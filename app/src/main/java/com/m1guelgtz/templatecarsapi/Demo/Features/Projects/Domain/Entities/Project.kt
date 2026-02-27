package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities

data class Project(
    val id: String,
    val name: String,
    val description: String?,
    val createdAt: String
)

data class ProjectMember(
    val userId: String,
    val username: String,
    val role: ProjectRole,
    val joinedAt: String
)

enum class ProjectRole {
    ADMIN, DEVELOPER
}
