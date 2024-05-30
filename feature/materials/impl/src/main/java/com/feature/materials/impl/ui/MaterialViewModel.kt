package com.feature.materials.impl.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.materials.api.model.Material
import com.feature.materials.impl.mapper.MaterialMapper
import com.itis.core.db.DatabaseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class MaterialViewState(
    val materialDataState: List<Material>? = null,
)

sealed interface MaterialEvent {
    object OnMaterialClick: MaterialEvent
}

sealed interface MaterialAction {
    object NavigateDetails: MaterialAction
}
@HiltViewModel
class MaterialViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(MaterialViewState())
    val state: StateFlow<MaterialViewState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<MaterialAction?>()
    val action: SharedFlow<MaterialAction?>
        get() = _action.asSharedFlow()

    fun event(materialEvent: MaterialEvent) {
        when (materialEvent) {
            MaterialEvent.OnMaterialClick -> onMaterialClick()
        }
    }

    private fun onMaterialClick() {
        viewModelScope.launch {
            _action.emit(MaterialAction.NavigateDetails)
        }
    }

}
