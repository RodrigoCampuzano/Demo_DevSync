package com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.Repository

import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.Entities.User

interface UserRepository {
    suspend fun listUsers(): List<User>
    suspend fun getUser(userId: String): User
}
