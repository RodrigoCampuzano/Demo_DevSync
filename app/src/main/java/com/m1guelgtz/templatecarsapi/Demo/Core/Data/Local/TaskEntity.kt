package com.m1guelgtz.templatecarsapi.Demo.Core.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String, // UUID
    val task_id: String?,       // ID amigable (ej: T-405)
    val projectId: String,
    val title: String,
    val description: String?,
    val status: String,
    val assignedTo: String?,
    val createdBy: String,
    val version: Int,
    val updatedAt: String,
    val createdAt: String
)
