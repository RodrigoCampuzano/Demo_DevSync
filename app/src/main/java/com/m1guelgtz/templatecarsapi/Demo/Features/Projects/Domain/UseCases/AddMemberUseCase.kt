package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.UseCases

import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Repository.ProjectRepository
import javax.inject.Inject

class AddMemberUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(projectId: String, userId: String, role: String) {
        repository.addMember(projectId, userId, role)
    }
}
