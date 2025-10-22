package com.example.roboranger.domain

import com.example.roboranger.data.local.Room.CommonFormData
import com.example.roboranger.data.local.Room.Forms_1
import com.example.roboranger.data.local.Room.Forms_2
import com.example.roboranger.domain.model.UIResources
import kotlinx.coroutines.flow.Flow

interface FormsDataBaseRepository {

    // --- Operaciones de Lectura con Flow ---
    fun getForm1ById(id: Int): Flow<UIResources<Forms_1>>
    fun getForm2ById(id: Int): Flow<UIResources<Forms_2>>
    fun getAllCommonForms(): Flow<UIResources<List<CommonFormData>>>
    fun getTotalSentFormsCount(): Flow<UIResources<Int>>

    // --- Operaciones de Escritura (suspend fun) ---
    suspend fun insertForm1(form: Forms_1): Long
    suspend fun insertForm2(form: Forms_2): Long
    suspend fun markForm1AsSent(formId: Int)
    suspend fun markForm2AsSent(formId: Int)
}