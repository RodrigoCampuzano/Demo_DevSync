package com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.Entities

data class User(
    val id: String,
    val username: String,
    val email: String,
    val token: String
)
