package com.example.roboranger.ui.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.data.FormCard
import com.example.roboranger.data.local.Room.CommonFormData
import com.example.roboranger.domain.model.UIResources
import com.example.roboranger.domain.usecase.local_data.GetAllCommonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCommonUseCase: GetAllCommonUseCase
): ViewModel() {

    private val _uiState= MutableStateFlow(UiState())

    val uiState = _uiState.asStateFlow()
    init {
        loadData()
    }

    fun loadData(){
        getAllCommonUseCase.invoke().onEach { list->
            when(list){
                UIResources.Loading -> {}
                is UIResources.Error -> {}
                is UIResources.Success<List<CommonFormData>> -> {
                    val formsEnv = list.data.filter { it.enviado }
                    val formsNo = list.data.filter { !it.enviado }
                    _uiState.update { current->
                        current.copy(
                            totalFormularios = formsEnv.size + formsNo.size,
                            totalFormulariosEnviados = formsEnv.size,
                            formulariosEnviados = formsEnv.map { it-> it.toFormCard() },
                            formulariosNoEnviados = formsNo.map { it-> it.toFormCard() }
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}

data class UiState(
    val totalFormularios: Int = 0,
    val totalFormulariosEnviados: Int = 0,
    val formulariosEnviados: List<FormCard> = emptyList<FormCard>(),
    val formulariosNoEnviados: List<FormCard> = emptyList<FormCard>()
)

fun CommonFormData.toFormCard(): FormCard = FormCard(
    id = id,
    name = nombre,
    date = fecha,
    hour = hora,
    formType = formType,
    status = enviado,
)