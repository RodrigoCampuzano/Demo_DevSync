package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Data.DataSource.Remote.Model

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("id") val id: String,
    @SerializedName("task_id") val taskId: String?, // Friendly ID (ej: T-405)
    @SerializedName("project_id") val projectId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("status") val status: String,
    @SerializedName("assigned_to") val assignedTo: String?,
    @SerializedName("created_by") val createdBy: String,
    @SerializedName("version") val version: Int,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("created_at") val createdAt: String
)

data class CreateTaskRequest(
    @SerializedName("project_id") val projectId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("created_by") val createdBy: String
)

data class TaskUpdate(
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("assigned_to") val assignedTo: String? = null,
    @SerializedName("version") val version: Int
)

data class UpdateTaskStatusRequest(
    @SerializedName("status") val status: String,
    @SerializedName("version") val version: Int
)

data class AssignTaskRequest(
    @SerializedName("user_id") val userId: String?
)
