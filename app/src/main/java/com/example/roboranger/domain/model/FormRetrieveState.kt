package com.example.roboranger.domain.model

import com.example.roboranger.data.local.Room.Forms_1
import com.example.roboranger.data.local.Room.Forms_2

sealed  interface FormRetrieveState{
    object Loading : FormRetrieveState
    data class SuccessForm1(val form: Forms_1) : FormRetrieveState
    data class SuccessForm2(val form: Forms_2) : FormRetrieveState
    data class Error(val message: String) : FormRetrieveState
}