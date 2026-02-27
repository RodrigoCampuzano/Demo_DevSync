package com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.Entities.User
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.UseCases.LoginUseCase
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.UseCases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

data class LoginUiState(
    val activeTab: String = "login",
    val showPassword: Boolean = false,
    val loginEmail: String = "",
    val loginPassword: String = "",
    val regUsername: String = "",
    val regEmail: String = "",
    val regPassword: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onTabChanged(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun onShowPasswordToggled() {
        _uiState.update { it.copy(showPassword = !it.showPassword) }
    }

    fun onLoginEmailChanged(email: String) {
        _uiState.update { it.copy(loginEmail = email) }
    }

    fun onLoginPasswordChanged(password: String) {
        _uiState.update { it.copy(loginPassword = password) }
    }

    fun onRegUsernameChanged(username: String) {
        _uiState.update { it.copy(regUsername = username) }
    }

    fun onRegEmailChanged(email: String) {
        _uiState.update { it.copy(regEmail = email) }
    }

    fun onRegPasswordChanged(password: String) {
        _uiState.update { it.copy(regPassword = password) }
    }

    fun login() {
        val state = _uiState.value
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = loginUseCase(state.loginEmail, state.loginPassword)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun register() {
        val state = _uiState.value
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = registerUseCase(state.regUsername, state.regEmail, state.regPassword)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }
}
