package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.UseCases

import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String) {
        repository.deleteTask(taskId)
    }
}
