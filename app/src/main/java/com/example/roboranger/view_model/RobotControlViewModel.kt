package com.example.roboranger.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.RetrofitClient
import kotlinx.coroutines.launch

class RobotControlViewModel: ViewModel() {
    val errorM = mutableStateOf<String?>(null)
    init{
        try_go_front()
    }
    fun try_go_front(){

        viewModelScope.launch {
            try{
                RetrofitClient.api.robot_go_front()
            }catch (e: Exception){
                errorM.value = "Error al recivir informacion: ${e.message}"
            }
        }
    }

}