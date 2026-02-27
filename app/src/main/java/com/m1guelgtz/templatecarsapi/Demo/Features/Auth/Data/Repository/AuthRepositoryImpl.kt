package com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Data.Repository

import com.m1guelgtz.templatecarsapi.Demo.Core.Network.DevSyncApi
import com.m1guelgtz.templatecarsapi.Demo.Core.SessionManager
import com.m1guelgtz.templatecarsapi.Demo.Core.UserSession
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Data.DataSource.Remote.Mapper.toDomain
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Data.DataSource.Remote.Model.CreateUserRequest
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Data.DataSource.Remote.Model.LoginRequest
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.Entities.User
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.Repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: DevSyncApi,
    private val sessionManager: SessionManager
) : AuthRepository {
    override suspend fun login(email: String, password: String): User {
        val response = api.login(LoginRequest(email, password))
        
        UserSession.token = response.accessToken
        sessionManager.token = response.accessToken
        
        try {
            val users = api.listUsers()
            val currentUser = users.find { it.email.lowercase() == email.lowercase() }
            if (currentUser != null) {
                UserSession.userId = currentUser.id
                UserSession.username = currentUser.username
                UserSession.email = currentUser.email
                
                sessionManager.userId = currentUser.id
                sessionManager.username = currentUser.username
                sessionManager.email = currentUser.email
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return response.toDomain()
    }

    override suspend fun register(username: String, email: String, password: String): User {
        val userDto = api.createUser(CreateUserRequest(username, email, password))
        return login(email, password)
    }
}
