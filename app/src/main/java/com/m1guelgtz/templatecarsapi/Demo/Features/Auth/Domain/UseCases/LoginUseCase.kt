package com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.UseCases

import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.Entities.User
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.Repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): User {
        return repository.login(email, password)
    }
}
