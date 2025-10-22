package com.example.roboranger.domain.usecase.local_data

import com.example.roboranger.domain.FormsDataBaseRepository
import com.example.roboranger.domain.model.FormRetrieveState
import com.example.roboranger.domain.model.UIResources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFormByIdUseCase @Inject constructor(
    private val formsDataBaseRepositoryImpl: FormsDataBaseRepository
) {
    operator fun invoke(id: Int, formType: Int): Flow<FormRetrieveState> {
        return when (formType) {
            1 -> formsDataBaseRepositoryImpl.getForm1ById(id).map { resource ->
                when (resource) {
                    is UIResources.Success -> FormRetrieveState.SuccessForm1(resource.data)
                    is UIResources.Error -> FormRetrieveState.Error(resource.message)
                    is UIResources.Loading -> FormRetrieveState.Loading
                }
            }
            2 -> formsDataBaseRepositoryImpl.getForm2ById(id).map { resource ->
                when (resource) {
                    is UIResources.Success -> FormRetrieveState.SuccessForm2(resource.data)
                    is UIResources.Error -> FormRetrieveState.Error(resource.message)
                    is UIResources.Loading -> FormRetrieveState.Loading
                }
            }
            else -> throw IllegalArgumentException("Invalid form type")

        }
    }
}
