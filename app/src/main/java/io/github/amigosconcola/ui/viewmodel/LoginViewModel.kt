package io.github.amigosconcola.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.amigosconcola.domain.model.Tokens
import io.github.amigosconcola.domain.use_case.Login
import io.github.amigosconcola.domain.use_case.ValidatePassword
import io.github.amigosconcola.domain.use_case.ValidateUsername
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
)

sealed class LoginScreenEvent {
    data class UsernameChanged(val username: String) : LoginScreenEvent()
    data class PasswordChanged(val password: String) : LoginScreenEvent()
    data object Submit : LoginScreenEvent()
}

sealed class LoginEvent {
    data class Success(val tokens: Tokens) : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    val validateUsername: ValidateUsername,
    val validatePassword: ValidatePassword,
    val login: Login
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _errors = Channel<String>()
    val errors = _errors.receiveAsFlow()

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onEvent(evt: LoginScreenEvent) {
        when (evt) {
            is LoginScreenEvent.UsernameChanged -> onUsernameChanged(evt.username)
            is LoginScreenEvent.PasswordChanged -> onPasswordChanged(evt.password)
            is LoginScreenEvent.Submit -> onSubmit()
        }
    }

    private fun onSubmit() {
        val username = validateUsername(_state.value.username)
        val password = validatePassword(_state.value.password)

        _state.update {
            it.copy(
                usernameError = username.exceptionOrNull()?.message,
                passwordError = password.exceptionOrNull()?.message
            )
        }

        if (username.isFailure || password.isFailure)
            return

        viewModelScope.launch {
            setLoading(true)
            val result = login(username.getOrThrow(), password.getOrThrow())
            setLoading(false)
            when {
                result.isSuccess -> _events.send(LoginEvent.Success(result.getOrThrow()))
                result.isFailure -> _errors.send(result.exceptionOrNull()?.message!!)
            }
        }
    }

    private fun setLoading(loading: Boolean) = _state.update {
        it.copy(
            isLoading = loading
        )
    }

    private fun onUsernameChanged(username: String) {
        _state.update {
            it.copy(
                username = username,
            )
        }
    }

    private fun onPasswordChanged(password: String) {
        _state.update {
            it.copy(
                password = password
            )
        }
    }


}