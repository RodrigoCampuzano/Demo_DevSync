package com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.UseCases

import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.Entities.User
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.Repository.UserRepository
import javax.inject.Inject

class ListUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): List<User> {
        return repository.listUsers()
    }
}
