package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.UseCases

import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.Task
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.TaskStatus
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(
        taskId: String,
        title: String?,
        description: String?,
        status: TaskStatus?,
        assignedTo: String?,
        version: Int
    ): Task {
        return repository.updateTask(taskId, title, description, status, assignedTo, version)
    }
}
