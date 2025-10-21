package com.example.roboranger.domain.usecase.local_data

import com.example.roboranger.data.local.Room.FormsRepository
import com.example.roboranger.data.local.Room.Forms_1
import com.example.roboranger.data.local.Room.Forms_2
import javax.inject.Inject

class InsertForm1UseCase @Inject constructor(
    private val formsRepository: FormsRepository
) {
    suspend operator fun invoke(form: Forms_1) {
        formsRepository.insertForm1(form)
    }
}

class InsertForm2UseCase @Inject constructor(
    private val formsRepository: FormsRepository
) {
    suspend operator fun invoke(form: Forms_2) {
        formsRepository.insertForm2(form)
    }
}