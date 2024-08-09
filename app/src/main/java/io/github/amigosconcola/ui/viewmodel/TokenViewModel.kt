package io.github.amigosconcola.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.amigosconcola.domain.model.Tokens
import io.github.amigosconcola.domain.repository.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TokenUiEvent {
    data class Store(val tokens: Tokens) : TokenUiEvent
    data object Delete : TokenUiEvent
}

sealed interface TokenEvent {
    data object Stored : TokenEvent
    data object Deleted : TokenEvent
}


@HiltViewModel
class TokenViewModel @Inject constructor(
    private val _tokens: TokenRepository
) : ViewModel() {
    companion object {
        private const val TAG = "TokenViewModel"
    }

    val tokens = _tokens.tokens()

    private val _events = Channel<TokenEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _tokens.loadTokens()
        }
    }

    fun onEvent(evt: TokenUiEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (evt) {
                is TokenUiEvent.Store -> {
                    _tokens.saveTokens(evt.tokens)
                    _events.send(TokenEvent.Stored)
                }

                is TokenUiEvent.Delete -> {
                    _tokens.deleteTokens()
                    _events.send(TokenEvent.Deleted)
                }
            }
        }
    }
}