package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.UseCases

import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.Task
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Repository.TaskRepository
import javax.inject.Inject

class AssignTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String, userId: String): Task {
        return repository.assignTask(taskId, userId)
    }
}
