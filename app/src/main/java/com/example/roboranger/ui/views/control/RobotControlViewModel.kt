package com.example.roboranger.ui.views.control

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.domain.model.SaveState
import com.example.roboranger.domain.usecase.StreamFramesUseCase
import com.example.roboranger.domain.usecase.control.CapturePhotoUseCase
import com.example.roboranger.domain.usecase.control.SetLightUseCase
import com.example.roboranger.domain.usecase.control.StopUseCase
import com.example.roboranger.domain.usecase.control.TryGoBackUseCase
import com.example.roboranger.domain.usecase.control.TryGoFrontUseCase
import com.example.roboranger.domain.usecase.control.TryGoLeftUseCase
import com.example.roboranger.domain.usecase.control.TryGoRightUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class RobotControlViewModel @Inject constructor(
    private val tryGoFrontUseCase: TryGoFrontUseCase,
    private val tryGoBackUseCase: TryGoBackUseCase,
    private val tryGoLeftUseCase: TryGoLeftUseCase,
    private val tryGoRightUseCase: TryGoRightUseCase,
    private val stopUseCase: StopUseCase,
    private val setLightUseCase: SetLightUseCase,
    private val streamFrames: StreamFramesUseCase,
    private val capturePhoto: CapturePhotoUseCase
): ViewModel() {

    // Estados del Stream y UI
    private val _receivedVideoBitmap = MutableStateFlow<Bitmap?> (null)
    val videoBitmap = _receivedVideoBitmap.asStateFlow()
    val errorM = mutableStateOf<String?>(null)

    var _savingState = MutableStateFlow<SaveState>(SaveState.Idle)
    val savingState = _savingState.asStateFlow()

    private val _timeLeft = MutableStateFlow(10)
    val lightState = mutableStateOf(false)

    // Jobs
    private var streamingJob: Job? = null
    private var timerJob: Job? = null
    private var saveFileJob: Job? = null

    init {
        // Apagar linterna al iniciar conexion
        viewModelScope.launch(Dispatchers.IO) {
            try{
                setLightUseCase(false)
                errorM.value = ""
                lightState.value = false
            }catch (e: Exception){
                errorM.value = "Error al iniciar control de luz: ${e.message}"
            }
        }
    }

    // Funcion helper para ejecutar comandos de movimiento
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

    // Funciones de movimiento del robot
    fun stop() = executeRobotCommand { stopUseCase() }
    fun tryGoFront() = executeRobotCommand { tryGoFrontUseCase() }
    fun tryGoBack() = executeRobotCommand { tryGoBackUseCase() }
    fun tryGoLeft() = executeRobotCommand { tryGoLeftUseCase() }
    fun tryGoRight() = executeRobotCommand { tryGoRightUseCase() }

    // Funcion para controlar el estado de la linterna
    fun toggleLight(){
        viewModelScope.launch {
            try{
                val newState = !lightState.value
                setLightUseCase(newState)
                lightState.value = newState
                errorM.value = ""
            }catch (e: Exception){
                errorM.value = "Error al recibir informacion: ${e.message}"
            }
        }

    }

    // Funciones de control de streaming
    fun startStreaming(){
        Log.d("streamDebug", if(streamingJob?.isActive == true) "Activo" else "No Activo" )
        if(streamingJob?.isActive == true) return

        streamingJob = viewModelScope.launch(Dispatchers.IO) {
            try{
                streamFrames()
                    .collectLatest { frame ->
                        _receivedVideoBitmap.value = frame
                        _timeLeft.update { 10 }
                }
            }catch (e: Exception){
                errorM.value = "Stream request failed: ${e.message}"
                Log.e("streamError", e.message ?: "Na")
            }
        }
        startTimer()
    }

    fun stopStreaming(){
        streamingJob?.cancel()
        streamingJob = null
        _receivedVideoBitmap.value = null
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
                _timeLeft.update { currentTime -> if (currentTime > 0) currentTime - 1 else 0 }
                if (_timeLeft.value <= 0) {
                    Log.e("streamError", "Restarting the Stream")
                    // Timer reached zero, restart the stream
                    restartStream()
                    // Reset timer for the next cycle
                    _timeLeft.update { 10 }
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


    // Funciones de captura de foto
    fun takePhoto() {
        val bitmapShot = videoBitmap.value ?: run {
            errorM.value = "No image received to save."
            return
        }
        if (saveFileJob?.isActive == true) {
            errorM.value = "Saving in progress..."
            return
        }
        saveFileJob = viewModelScope.launch {
            _savingState.value = SaveState.Saving
            val name = "roboranger_capture_${System.currentTimeMillis()}.jpg"
            capturePhoto(bitmapShot, name).collect { state ->
                _savingState.value = state
            }
        }
    }

    fun resetCameraPhotoState(){
        _savingState.value = SaveState.Idle
    }
}