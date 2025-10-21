package com.example.roboranger.domain.usecase.local_data

import com.example.roboranger.data.FormsRepositoryImpl
import com.example.roboranger.data.local.Room.Forms_1
import com.example.roboranger.data.local.Room.Forms_2
import com.example.roboranger.domain.model.UIResources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFormByIdUseCase @Inject constructor(
    private val formsRepositoryImpl: FormsRepositoryImpl
) {
    operator fun invoke(id: Int, formType: Int): Flow<FormResult> {
        return when (formType) {
            1 -> formsRepositoryImpl.getForm1ById(id).map { resource ->
                when (resource) {
                    is UIResources.Success -> FormResult.Form1Result(resource.data)
                    is UIResources.Error -> FormResult.Error(resource.message)
                    is UIResources.Loading -> FormResult.Loading
                }
            }
            2 -> formsRepositoryImpl.getForm2ById(id).map { resource ->
                when (resource) {
                    is UIResources.Success -> FormResult.Form2Result(resource.data)
                    is UIResources.Error -> FormResult.Error(resource.message)
                    is UIResources.Loading -> FormResult.Loading
                }
            }
            else -> throw IllegalArgumentException("Invalid form type")

        }
    }
}
sealed class FormResult {
    data class Form1Result(val form: Forms_1) : FormResult()
    data class Form2Result(val form: Forms_2) : FormResult()
    data class Error(val message: String): FormResult()
    data object Loading: FormResult()
}
