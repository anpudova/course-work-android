package com.feature.home.impl.ui.game

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.home.api.model.User
import com.feature.home.impl.ui.game.mapper.UserMapper
import com.itis.core.db.DatabaseHandler
import com.itis.core.db.entity.UserEntity
import com.itis.core.utils.PreferencesManager
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
data class GameViewState(
    val recordState: Int = 0
)
sealed interface GameEvent {
    object OnNavigateHome : GameEvent
    data class GetRecord(val username: String): GameEvent
    data class SetRecord(val username: String, val record: Int): GameEvent

}

sealed interface GameAction {
    object NavigateHome : GameAction

}

@HiltViewModel
class GameViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(GameViewState())
    val state: StateFlow<GameViewState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<GameAction?>()
    val action: SharedFlow<GameAction?>
        get() = _action.asSharedFlow()

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    fun event(event: GameEvent) {
        when (event) {
            GameEvent.OnNavigateHome -> onNavigateHome()
            is GameEvent.GetRecord -> onGetRecord(event)
            is GameEvent.SetRecord -> onSetRecord(event)
        }
    }

    private fun onNavigateHome() {
        viewModelScope.launch {
            _action.emit(GameAction.NavigateHome)
        }
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun onGetRecord(event: GameEvent.GetRecord) {
        viewModelScope.launch {
            val user: User? = UserMapper.mapUserModel(DatabaseHandler.getUserByUsername(event.username))
            if (user != null) {
                _state.emit(_state.value.copy(recordState = user.record))
                val preferencesManager = PreferencesManager(context)
                preferencesManager.saveDataLong("record", user.record.toLong())
            }
        }
    }

    private fun onSetRecord(event: GameEvent.SetRecord) {
        viewModelScope.launch {
            DatabaseHandler.updateUserRecord(event.username, event.record)
        }
    }

}