package com.example.roboranger.data

data class FormCard(
    val id: Int,
    val name: String,
    val date: String,
    val hour: String,
    val formType: Int,
    val status: Boolean // 0 = enviado, 1 = guardado
)
