package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.UseCases

import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities.Project
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Repository.ProjectRepository
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(name: String, description: String?): Project {
        return repository.createProject(name, description)
    }
}
