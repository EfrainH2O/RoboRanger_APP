package com.example.roboranger.data

data class FormCard(
    val id: Int,
    val place: String,
    val date: String,
    val hour: String,
    val status: Int // 0 = enviado, 1 = guardado
)
