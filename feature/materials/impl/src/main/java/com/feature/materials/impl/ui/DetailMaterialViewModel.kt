package com.feature.materials.impl.ui

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.materials.api.model.Material
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Immutable
data class DetailMaterialViewState(
    val detailDataState: Material? = null,
    val errorState: Throwable? = null,
)

sealed interface DetailMaterialEvent {
    object OnNavigateBack: DetailMaterialEvent
}

sealed interface DetailMaterialAction {
    object NavigateBack : DetailMaterialAction
}

@HiltViewModel
class DetailMaterialViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(DetailMaterialViewState())
    val state: StateFlow<DetailMaterialViewState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<DetailMaterialAction?>()
    val action: SharedFlow<DetailMaterialAction?>
        get() = _action.asSharedFlow()

    fun event(event: DetailMaterialEvent) {
        when (event) {
            DetailMaterialEvent.OnNavigateBack -> onNavigateBack()
        }
    }

    private fun onNavigateBack() {
        viewModelScope.launch {
            _action.emit(DetailMaterialAction.NavigateBack)
        }
    }

}
