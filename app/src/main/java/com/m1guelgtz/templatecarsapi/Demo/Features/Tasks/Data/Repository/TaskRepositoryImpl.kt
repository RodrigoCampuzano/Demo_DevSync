package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Data.Repository

import com.m1guelgtz.templatecarsapi.Demo.Core.Data.Local.TaskDao
import com.m1guelgtz.templatecarsapi.Demo.Core.Network.DevSyncApi
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Data.DataSource.Remote.Mapper.toDomain
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Data.DataSource.Remote.Mapper.toEntity
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Data.DataSource.Remote.Model.*
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.Task
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.TaskStatus
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val api: DevSyncApi,
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getTasksByProjectFlow(projectId: String): Flow<List<Task>> {
        return taskDao.getTasksByProject(projectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTasksFlow(): Flow<List<Task>> {
        return taskDao.getAllTasksFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTasksByProject(projectId: String): List<Task> {
        return api.getTasksByProject(projectId).map { it.toDomain() }
    }

    override suspend fun syncTasksFromServer(projectId: String) {
        try {
            val remoteTasks = api.getTasksByProject(projectId)
            taskDao.deleteTasksByProject(projectId)
            taskDao.insertTasks(remoteTasks.map { it.toEntity() })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun createTask(projectId: String, title: String, description: String?, createdBy: String): Task {
        val task = api.createTask(CreateTaskRequest(projectId, title, description, createdBy))
        taskDao.insertTask(task.toEntity())
        return task.toDomain()
    }

    override suspend fun getTask(taskId: String): Task {
        return api.getTask(taskId).toDomain()
    }

    override suspend fun updateTask(
        taskId: String,
        title: String?,
        description: String?,
        status: TaskStatus?,
        assignedTo: String?,
        version: Int
    ): Task {
        val updatedTask = api.updateTask(taskId, TaskUpdate(title, description, status?.name, assignedTo, version))
        taskDao.insertTask(updatedTask.toEntity())
        return updatedTask.toDomain()
    }

    override suspend fun deleteTask(taskId: String) {
        api.deleteTask(taskId)
        taskDao.deleteTask(taskId)
    }

    override suspend fun updateTaskStatus(taskId: String, status: TaskStatus, version: Int): Task {
        val updatedTask = api.updateTaskStatus(taskId, UpdateTaskStatusRequest(status.name, version))
        taskDao.insertTask(updatedTask.toEntity())
        return updatedTask.toDomain()
    }

    override suspend fun assignTask(taskId: String, userId: String): Task {
        val updatedTask = api.assignTask(taskId, AssignTaskRequest(userId))
        taskDao.insertTask(updatedTask.toEntity())
        return updatedTask.toDomain()
    }
}
