package com.example.roboranger.ui.views.control

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.RetrofitClient
import com.example.roboranger.StreamClient
import com.example.roboranger.util.BitmapSaver
import com.example.roboranger.util.MjpegInputStream
import com.example.roboranger.util.SaveState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RobotControlViewModel: ViewModel() {

    // -- values
    private val receivedVideoBitmap = MutableStateFlow<Bitmap?> (null)
    val videoBitmap = receivedVideoBitmap.asStateFlow()
    val errorM = mutableStateOf<String?>(null)

    var _savingState = MutableStateFlow<SaveState>(SaveState.Idle)
    val savingState = _savingState.asStateFlow()
    private val _time_left = MutableStateFlow(10)
    val lightState = mutableStateOf(false)
    // -- Jobs
    private var streamingJob: Job? = null
    private var timerJob: Job? = null
    private var saveFileJob: Job? = null
    // -- Functions

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                RetrofitClient.api.turnOffLight()
                errorM.value = ""
            }catch (e: Exception){
                errorM.value = "Error al recibir informacion: ${e.message}"
            }
        }
    }

    private fun executeRobotCommand(command: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                command()
                withContext(Dispatchers.Main) {
                    errorM.value = "" // Clear error on success
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorM.value = "Error de comunicación: ${e.message}"
                }
            }
        }
    }

    fun stop() = executeRobotCommand { RetrofitClient.api.robotStop() }
    fun tryGoFront() = executeRobotCommand { RetrofitClient.api.robotGoFront() }
    fun tryGoBack() = executeRobotCommand { RetrofitClient.api.robotGoBack() }
    fun tryGoLeft() = executeRobotCommand { RetrofitClient.api.robotGoLeft() }
    fun tryGoRight() = executeRobotCommand { RetrofitClient.api.robotGoRight() }

    fun toggleLight(){
        viewModelScope.launch {

            try{
                if (!lightState.value){
                    val l = RetrofitClient.api.turnOnLight()
                    if(l.isSuccessful){
                        lightState.value = true
                    }
                }else{
                    val l = RetrofitClient.api.turnOffLight()
                    if(l.isSuccessful){
                        lightState.value = false
                    }
                }
                errorM.value = ""
            }catch (e: Exception){
                errorM.value = "Error al recibir informacion: ${e.message}"
            }
        }

    }


    fun startStreaming(){
        Log.d("streamDebug", if(streamingJob?.isActive == true) "activo" else "No Activo" )
        if(streamingJob?.isActive == true) return

        streamingJob = viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = StreamClient.api.cameraStream().execute()

                if(response.isSuccessful && response.body() != null){
                    val inputStream = response.body()!!.byteStream()

                    // Wrap it with our new MJPEG parser
                    val mjpegInputStream = MjpegInputStream(inputStream)
                    while(isActive){
                        val bitmap = mjpegInputStream.readMjpegFrame()
                        if(bitmap != null){
                            receivedVideoBitmap.value = bitmap
                            _time_left.update { 10 }
                        }else{
                            Log.e("streamError", "bitmapNull")
                            break
                        }
                    }
                } else {
                    // Use postValue when updating LiveData from a background thread.
                    // For StateFlow, .value is thread-safe.
                    errorM.value = "Stream request failed: ${response.code()}"
                    errorM.value?.let { it ->
                        Log.e("streamError", it)
                    }

                }

            }catch (e: Exception){
                Log.e("streamError", e.message ?: "Na" )
            }
        }
        startTimer()

    }

    fun stopStreaming(){
        streamingJob?.cancel()
        streamingJob = null
        receivedVideoBitmap.value = null
    }

    override fun onCleared() {
        stopStreaming()
        super.onCleared()

    }

    private fun startTimer() {
        timerJob?.cancel() // Cancel any existing timer
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                // Decrement the timer. Use .update for safe modification.
                _time_left.update { currentTime -> if (currentTime > 0) currentTime - 1 else 0 }

                if (_time_left.value <= 0) {
                    Log.e("streamError", "Restarting the Stream")
                    // Timer reached zero, restart the stream
                    restartStream()
                    // Reset timer for the next cycle
                    _time_left.update { 10 }
                }
            }
        }
    }

    private fun restartStream() {
        // This runs on the main thread from the timer
        viewModelScope.launch {
            // Stop the current stream
            streamingJob?.cancel()
            // Short delay to allow resources to be released
            delay(100L)
            // Start a new stream
            startStreaming()
        }
    }



    fun takePhoto(context  : Context){
        val bitmapShot = videoBitmap.value
        if(bitmapShot == null){
            Log.d("cameraCapture", "NO IMAGE TO SAVE")
            Toast.makeText(context, "No image received to save.", Toast.LENGTH_SHORT).show()
            return;
        }

        if (saveFileJob?.isActive == true) {
            Log.d("cameraCapture", "A save operation is already in progress.")
            Toast.makeText(context, "Saving in progress...", Toast.LENGTH_SHORT).show()
            return
        }

        saveFileJob = viewModelScope.launch {
            _savingState.value = SaveState.Saving
            val time = System.currentTimeMillis()
            val disp_image_name = "roboranger_capture_$time.jpg"
            BitmapSaver.saveBitmap(context, bitmapShot, disp_image_name).collect {
                state -> _savingState.value = state
            }
            Toast.makeText(context, "Imaged Saved", Toast.LENGTH_SHORT).show()
            delay(100L)
        }
    }

    fun resetCameraPhotoState(){
        _savingState.value = SaveState.Idle
    }



}