package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities

data class Task(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val assignedTo: String?,
    val createdBy: String,
    val version: Int,
    val updatedAt: String,
    val createdAt: String
)

enum class TaskStatus {
    TODO, IN_PROGRESS, REVIEW, DONE
}
