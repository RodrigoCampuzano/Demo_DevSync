package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Data.Repository

import com.m1guelgtz.templatecarsapi.Demo.Core.Network.DevSyncApi
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Data.DataSource.Remote.Mapper.toDomain
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Data.DataSource.Remote.Model.AddMemberRequest
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Data.DataSource.Remote.Model.CreateProjectRequest
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Data.DataSource.Remote.Model.ProjectUpdate
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities.Project
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities.ProjectMember
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Repository.ProjectRepository
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val api: DevSyncApi
) : ProjectRepository {
    override suspend fun listProjects(): List<Project> {
        return api.listProjects().map { it.toDomain() }
    }

    override suspend fun createProject(name: String, description: String?): Project {
        return api.createProject(CreateProjectRequest(name, description)).toDomain()
    }

    override suspend fun getProject(projectId: String): Project {
        return api.getProject(projectId).toDomain()
    }

    override suspend fun updateProject(projectId: String, name: String?, description: String?): Project {
        return api.updateProject(projectId, ProjectUpdate(name, description)).toDomain()
    }

    override suspend fun deleteProject(projectId: String) {
        api.deleteProject(projectId)
    }

    override suspend fun listMembers(projectId: String): List<ProjectMember> {
        return api.listMembers(projectId).map { it.toDomain() }
    }

    override suspend fun addMember(projectId: String, userId: String, role: String) {
        api.addMember(projectId, AddMemberRequest(userId, role))
    }
}
