package com.example.roboranger.ui.views.control

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class RobotControlViewModel: ViewModel() {
    val errorM = mutableStateOf<String?>(null)
    val lightState = mutableStateOf(false)

    fun stop(){
        viewModelScope.launch {
            try{
                RetrofitClient.api.robotStop()
            }catch (e: Exception){
                errorM.value = "Error al recibir informacion: ${e.message}"
            }
        }
    }
    fun tryGoFront(){
        viewModelScope.launch {
            try{
                RetrofitClient.api.robotGoFront()
            }catch (e: Exception){
                errorM.value = "Error al recibir informacion: ${e.message}"
            }
        }
    }


    fun tryGoBack(){
        viewModelScope.launch {
            try{
                RetrofitClient.api.robotGoBack()
            }catch (e: Exception){
                errorM.value = "Error al recibir informacion: ${e.message}"
            }
        }
    }

    fun tryGoLeft(){
        viewModelScope.launch {
            try{
                RetrofitClient.api.robotGoLeft()
            }catch (e: Exception){
                errorM.value = "Error al recibir informacion: ${e.message}"
            }
        }
    }

    fun tryGoRight(){
        viewModelScope.launch {
            try{
                RetrofitClient.api.robotGoRight()
            }catch (e: Exception){
                errorM.value = "Error al recibir informacion: ${e.message}"
            }
        }
    }

    fun toggleLight(){
        fun tryGoBack(){
            viewModelScope.launch {
                try{
                    if (!lightState.value){
                        val l = RetrofitClient.api.turnOnLight()
                        if(l != Unit){
                            lightState.value = true
                        }
                    }else{
                        val l = RetrofitClient.api.turnOffLight()
                        if(l != Unit){
                            lightState.value = false
                        }
                    }
                }catch (e: Exception){
                    errorM.value = "Error al recibir informacion: ${e.message}"
                }
            }
        }
    }
}