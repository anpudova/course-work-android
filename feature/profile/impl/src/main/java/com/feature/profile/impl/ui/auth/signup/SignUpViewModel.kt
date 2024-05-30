package com.feature.profile.impl.ui.auth.signup

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.profile.api.model.User
import com.feature.profile.impl.mapper.UserMapper
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
data class SignUpViewState(
    val messageState: String = ""
)

sealed interface SignUpEvent {
    data class OnSignUpClick(val id: Long, val username: String, val password: String, val rpassword: String):
        SignUpEvent
}

sealed interface SignUpAction {
    object NavigateSignIn: SignUpAction
}

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SignUpViewState())
    val state: StateFlow<SignUpViewState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<SignUpAction?>()
    val action: SharedFlow<SignUpAction?>
        get() = _action.asSharedFlow()

    fun event(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnSignUpClick -> onSignUpClick(event)
        }
    }

    private fun onSignUpClick(event: SignUpEvent.OnSignUpClick) {
        viewModelScope.launch {
            if (event.password == event.rpassword) {
                val regPass = "^[a-zA-Z0-9]{8,20}$".toRegex()
                val regName = "^[a-zA-Z]{2,20}$".toRegex()
                if (regPass.matchEntire(event.password) != null &&
                    regName.matchEntire(event.username) != null) {
                    val username: String? = DatabaseHandler.getUsername(event.username)
                    if (username == null) {
                        val user = User(event.id, event.username, event.password, 0)
                        DatabaseHandler.createUser(UserMapper.mapUserEntity(user))
                        _action.emit(SignUpAction.NavigateSignIn)
                    } else {
                        _state.emit(_state.value.copy(messageState = "Это имя пользователя уже используется"))
                    }
                } else {
                    _state.emit(_state.value.copy(messageState = "Некорректно введен логин/пароль (пароль 8-20 символов a-zA-Z0-9, логин 2-20 символов)"))
                }
            } else {
                _state.emit(_state.value.copy(messageState = "Пароли не совпадают"))
            }
        }
    }
}