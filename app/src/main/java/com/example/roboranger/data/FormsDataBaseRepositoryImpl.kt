package com.example.roboranger.data

import android.util.Log
import com.example.roboranger.data.local.Room.CommonFormData
import com.example.roboranger.data.local.Room.Forms_1
import com.example.roboranger.data.local.Room.Forms_2
import com.example.roboranger.data.local.Room.IFormsDao
import com.example.roboranger.domain.FormsDataBaseRepository
import com.example.roboranger.domain.model.UIResources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormsDataBaseRepositoryImpl @Inject constructor(
    private val formsDao: IFormsDao
) : FormsDataBaseRepository {

    override fun getForm1ById(id: Int): Flow<UIResources<Forms_1>> = flow {
        emit(UIResources.Loading)
        val form = formsDao.getForms1_AllInfo(id)
        if (form != null) {
            emit(UIResources.Success(form))
        } else {
            emit(UIResources.Error("Formulario no encontrado con id: $id"))
        }
    }.catch { e ->
        emit(UIResources.Error(e.localizedMessage ?: "Error desconocido"))
    }

    override fun getForm2ById(id: Int): Flow<UIResources<Forms_2>> = flow {
        emit(UIResources.Loading)
        val form = formsDao.getForms2_AllInfo(id)
        if (form != null) {
            emit(UIResources.Success(form))
        } else {
            emit(UIResources.Error("Formulario no encontrado con id: $id"))
        }
    }.catch { e ->
        emit(UIResources.Error(e.localizedMessage ?: "Error desconocido"))
    }

    override fun getAllCommonForms(): Flow<UIResources<List<CommonFormData>>> = flow {
        emit(UIResources.Loading)
        val forms = formsDao.getAllCommonForms()
        emit(UIResources.Success(forms))
    }.catch { e ->
        emit(UIResources.Error(e.localizedMessage ?: "Error desconocido"))
    }

    override fun getTotalSentFormsCount(): Flow<UIResources<Int>> = flow {
        emit(UIResources.Loading)
        val count = formsDao.getFormsCount()
        emit(UIResources.Success(count))
    }.catch { e ->
        emit(UIResources.Error(e.localizedMessage ?: "Error desconocido"))
    }

    // Las operaciones de escritura lanzan la excepción para que el ViewModel la maneje.
    override suspend fun insertForm1(form: Forms_1) : Long{
        try {
            val id = formsDao.insertForm1(form)
            Log.d("DB_Manager", "Insert successful")
            return id
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun insertForm2(form: Forms_2) : Long{
        try {
            val id = formsDao.insertForm2(form)
            Log.d("DB_Manager", "Insert successful")
            return id
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun markForm1AsSent(formId: Int) {
        try {
            formsDao.markForm1AsSent(formId)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun markForm2AsSent(formId: Int) {
        try {
            formsDao.markForm2AsSent(formId)
        } catch (e: Exception) {
            throw e
        }
    }
}