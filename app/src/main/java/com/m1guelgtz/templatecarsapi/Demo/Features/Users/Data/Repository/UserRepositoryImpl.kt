package com.m1guelgtz.templatecarsapi.Demo.Features.Users.Data.Repository

import com.m1guelgtz.templatecarsapi.Demo.Core.Network.DevSyncApi
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Data.DataSource.Remote.Mapper.toDomain
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.Entities.User
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.Repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: DevSyncApi
) : UserRepository {
    override suspend fun listUsers(): List<User> {
        return api.listUsers().map { it.toDomain() }
    }

    override suspend fun getUser(userId: String): User {
        return api.getUser(userId).toDomain()
    }
}
