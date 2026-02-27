package com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.Repository

import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.Entities.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(username: String, email: String, password: String): User
}
