package com.feature.home.impl.ui.game

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@Immutable
data class LobbyViewState(
    val lobbyDataState: List<String>? = null,
)

sealed interface LobbyEvent {
}

sealed interface LobbyAction {
}

@HiltViewModel
class LobbyViewModel @Inject constructor() : ViewModel() {

    private val _action = MutableSharedFlow<LobbyAction?>()
    val action: SharedFlow<LobbyAction?>
        get() = _action.asSharedFlow()

    fun event(event: LobbyEvent) {
    }


}