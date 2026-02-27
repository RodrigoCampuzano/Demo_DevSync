package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.UseCases

import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.Task
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(projectId: String, title: String, description: String?, createdBy: String): Task {
        return repository.createTask(projectId, title, description, createdBy)
    }
}
