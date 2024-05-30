package com.feature.home.impl.ui.game

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

sealed interface ResultEvent {
}

sealed interface ResultAction {

}

@HiltViewModel
class ResultViewModel @Inject constructor() : ViewModel() {

    private val _action = MutableSharedFlow<ResultAction?>()
    val action: SharedFlow<ResultAction?>
        get() = _action.asSharedFlow()

    fun event(event: ResultEvent) {
    }
}