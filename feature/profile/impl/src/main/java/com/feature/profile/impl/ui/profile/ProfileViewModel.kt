package com.feature.profile.impl.ui.profile

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.core.db.DatabaseHandler
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class ProfileViewState(
    val dataState: Boolean = false,
    val recordState: Int = 0
)

sealed interface ProfileEvent {
    data class OnDeleteAccClick(val username: String): ProfileEvent
    data class GetRecord(val username: String): ProfileEvent
    data class IsSignIn(val username: String): ProfileEvent
    object OnSignInClick: ProfileEvent
    object OnProfileClick: ProfileEvent
}

sealed interface ProfileAction {
    object NavigateProfile: ProfileAction
    object NavigateSignIn: ProfileAction
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileViewState())
    val state: StateFlow<ProfileViewState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<ProfileAction?>()
    val action: SharedFlow<ProfileAction?>
        get() = _action.asSharedFlow()

    fun event(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnDeleteAccClick -> onDeleteAccClick(event)
            is ProfileEvent.IsSignIn -> isSignIn(event)
            is ProfileEvent.GetRecord -> onGetRecord(event)
            ProfileEvent.OnSignInClick -> onNavigateSignIn()
            ProfileEvent.OnProfileClick -> onNavigateProfile()

        }
    }

    private fun onDeleteAccClick(event: ProfileEvent.OnDeleteAccClick) {
        viewModelScope.launch {
            val user = DatabaseHandler.getUserByUsername(event.username)
            user?.let { DatabaseHandler.deleteUser(it) }

        }
    }

    private fun onGetRecord(event: ProfileEvent.GetRecord) {
        viewModelScope.launch {
            val user = DatabaseHandler.getUserByUsername(event.username)
            user?.let { _state.emit(_state.value.copy(recordState = user.record)) }
        }
    }
    private fun onNavigateSignIn() {
        viewModelScope.launch {
            _action.emit(ProfileAction.NavigateSignIn)
        }
    }

    private fun onNavigateProfile() {
        viewModelScope.launch {
            _action.emit(ProfileAction.NavigateProfile)
        }
    }

    private fun isSignIn(event: ProfileEvent.IsSignIn) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(dataState = event.username != ""))
        }
    }
}