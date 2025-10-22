package com.example.roboranger.domain.usecase.local_data

import com.example.roboranger.data.local.Room.Forms_1
import com.example.roboranger.data.local.Room.Forms_2
import com.example.roboranger.domain.FormsDataBaseRepository
import javax.inject.Inject

class InsertForm1UseCase @Inject constructor(
    private val formsRepository: FormsDataBaseRepository
) {
    suspend operator fun invoke(form: Forms_1): Long {
        return formsRepository.insertForm1(form)
    }
}

class InsertForm2UseCase @Inject constructor(
    private val formsRepository: FormsDataBaseRepository
) {
    suspend operator fun invoke(form: Forms_2) : Long{
        return formsRepository.insertForm2(form)
    }
}