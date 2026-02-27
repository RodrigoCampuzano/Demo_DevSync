package com.m1guelgtz.templatecarsapi.Demo.Features.Users.Data.DataSource.Remote.Mapper

import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Data.DataSource.Remote.Model.UserDto
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.Entities.User

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        username = this.username,
        email = this.email,
        createdAt = this.createdAt
    )
}
