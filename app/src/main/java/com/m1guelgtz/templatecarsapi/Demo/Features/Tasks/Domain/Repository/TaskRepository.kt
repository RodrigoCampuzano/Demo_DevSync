package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Repository

import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.Task
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.TaskStatus
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasksByProjectFlow(projectId: String): Flow<List<Task>>
    fun getAllTasksFlow(): Flow<List<Task>>
    suspend fun getTasksByProject(projectId: String): List<Task>
    suspend fun syncTasksFromServer(projectId: String)
    suspend fun createTask(projectId: String, title: String, description: String?, createdBy: String): Task
    suspend fun getTask(taskId: String): Task
    suspend fun updateTask(taskId: String, title: String?, description: String?, status: TaskStatus?, assignedTo: String?, version: Int): Task
    suspend fun deleteTask(taskId: String)
    suspend fun updateTaskStatus(taskId: String, status: TaskStatus, version: Int): Task
    suspend fun assignTask(taskId: String, userId: String): Task
}
