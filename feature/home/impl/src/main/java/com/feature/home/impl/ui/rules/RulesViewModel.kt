package com.feature.home.impl.ui.rules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RulesEvent {
    object OnNavigateBack : RulesEvent
}

sealed interface RulesAction {
    object NavigateBack : RulesAction

}

@HiltViewModel
class RulesViewModel @Inject constructor() : ViewModel() {

    private val _action = MutableSharedFlow<RulesAction?>()
    val action: SharedFlow<RulesAction?>
        get() = _action.asSharedFlow()

    fun event(event: RulesEvent) {
        when (event) {
            RulesEvent.OnNavigateBack -> onNavigateBack()
        }
    }

    private fun onNavigateBack() {
        viewModelScope.launch {
            _action.emit(RulesAction.NavigateBack)
        }
    }
}