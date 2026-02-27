package com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Data.DataSource.Remote.Mapper

import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Data.DataSource.Remote.Model.AuthResponse
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.Entities.User

fun AuthResponse.toDomain(): User {
    return User(
        id = "", // API doesn't return user info in login, only token
        username = "",
        email = "",
        token = this.accessToken
    )
}
