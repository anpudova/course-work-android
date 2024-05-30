package com.feature.home.impl.ui.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
data class HomeViewState(
    val dataState: Boolean = false
)
sealed interface HomeEvent {
    object OnNavigateSingleGame : HomeEvent
    object OnNavigateGroupGame : HomeEvent
    object OnNavigateLobby : HomeEvent
    object OnNavigateRules : HomeEvent
    data class IsSignIn(val username: String): HomeEvent
    object OnSignInClick: HomeEvent
}

sealed interface HomeAction {
    object NavigateRules : HomeAction
    object NavigateSingleGame : HomeAction
    object NavigateGroupGame : HomeAction
    object NavigateLobby : HomeAction
    object NavigateSignIn: HomeAction

}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<HomeAction?>()
    val action: SharedFlow<HomeAction?>
        get() = _action.asSharedFlow()

    fun event(event: HomeEvent) {
        when (event) {
            HomeEvent.OnNavigateRules -> onNavigateRules()
            HomeEvent.OnNavigateSingleGame -> onNavigateSingleGame()
            HomeEvent.OnNavigateGroupGame -> onNavigateGroupGame()
            HomeEvent.OnNavigateLobby -> onNavigateLobby()
            HomeEvent.OnSignInClick -> onNavigateSignIn()
            is HomeEvent.IsSignIn -> isSignIn(event)
        }
    }

    private fun onNavigateRules() {
        viewModelScope.launch {
            _action.emit(HomeAction.NavigateRules)
        }
    }

    private fun onNavigateSingleGame() {
        viewModelScope.launch {
            _action.emit(HomeAction.NavigateSingleGame)
        }
    }
    private fun onNavigateGroupGame() {
        viewModelScope.launch {
            _action.emit(HomeAction.NavigateGroupGame)
        }
    }
    private fun onNavigateLobby() {
        viewModelScope.launch {
            _action.emit(HomeAction.NavigateLobby)
        }
    }

    private fun isSignIn(event: HomeEvent.IsSignIn) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(dataState = event.username != ""))
        }
    }

    private fun onNavigateSignIn() {
        viewModelScope.launch {
            _action.emit(HomeAction.NavigateSignIn)
        }
    }
}