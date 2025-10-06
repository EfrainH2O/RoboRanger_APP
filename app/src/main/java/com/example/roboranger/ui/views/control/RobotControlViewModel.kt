package com.example.roboranger.ui.views.control

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.RetrofitClient
import com.example.roboranger.StreamClient
import com.example.roboranger.util.MjpegInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RobotControlViewModel: ViewModel() {

    private val recivedVideoBitmap = MutableStateFlow<Bitmap?> (null)
    val videoBitmap = recivedVideoBitmap.asStateFlow()
    val errorM = mutableStateOf<String?>(null)
    val lightState = mutableStateOf(false)

    private var streamingJob: Job? = null
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
                    Log.d("xd","granxd")
                    while(isActive){
                        val bitmap = mjpegInputStream.readMjpegFrame()
                        if(bitmap != null){
                            recivedVideoBitmap.value = bitmap
                            Log.d("streamDebug", "bitmap is not null")
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

    }

    fun stopStreaming(){
        streamingJob?.cancel()
        streamingJob = null
    }

    override fun onCleared() {
        stopStreaming()
        super.onCleared()

    }




}