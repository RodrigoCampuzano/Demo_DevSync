package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.UseCases

import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.Task
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Repository.TaskRepository
import javax.inject.Inject

class GetTasksByProjectUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(projectId: String): List<Task> {
        return repository.getTasksByProject(projectId)
    }
}
