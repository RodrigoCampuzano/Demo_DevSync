package com.m1guelgtz.templatecarsapi.Demo.Features.Users.Presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.Entities.User
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.UseCases.ListUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserListState {
    object Loading : UserListState()
    data class Success(val users: List<User>) : UserListState()
    data class Error(val message: String) : UserListState()
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val listUsersUseCase: ListUsersUseCase
) : ViewModel() {

    private val _usersState = MutableStateFlow<UserListState>(UserListState.Loading)
    val usersState: StateFlow<UserListState> = _usersState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = UserListState.Loading
            try {
                val users = listUsersUseCase()
                _usersState.value = UserListState.Success(users)
            } catch (e: Exception) {
                _usersState.value = UserListState.Error(e.message ?: "Failed to load users")
            }
        }
    }
}
