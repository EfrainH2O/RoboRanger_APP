package com.example.roboranger.domain.usecase.local_data

import com.example.roboranger.domain.FormsDataBaseRepository
import javax.inject.Inject

class MarkFormAsSentUseCase @Inject constructor(
    private val formsRepository: FormsDataBaseRepository
) {
    suspend operator fun invoke(formId: Int, formType: Int) {
        when (formType) {
            1 -> formsRepository.markForm1AsSent(formId)
            2 -> formsRepository.markForm2AsSent(formId)
            else -> throw IllegalArgumentException("Invalid form type")
        }
    }
}