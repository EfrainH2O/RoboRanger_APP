package com.example.roboranger.domain.usecase.local_data

import com.example.roboranger.domain.FormsDataBaseRepository
import com.example.roboranger.domain.model.UIResources
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalFormsSentUseCase @Inject constructor(
    private val formsRepository: FormsDataBaseRepository
) {
    operator fun invoke(): Flow<UIResources<Int>>{
        return formsRepository.getTotalSentFormsCount()
    }
}