package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Data.DataSource.Remote.Model

import com.google.gson.annotations.SerializedName

data class ProjectDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("created_at") val createdAt: String
)

data class CreateProjectRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?
)

data class ProjectUpdate(
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null
)

data class ProjectMemberDto(
    @SerializedName("user_id") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("role") val role: String,
    @SerializedName("joined_at") val joinedAt: String
)

data class AddMemberRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("role") val role: String
)
