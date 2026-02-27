package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.UseCases

import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Repository.ProjectRepository
import javax.inject.Inject

class DeleteProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(projectId: String) {
        repository.deleteProject(projectId)
    }
}
