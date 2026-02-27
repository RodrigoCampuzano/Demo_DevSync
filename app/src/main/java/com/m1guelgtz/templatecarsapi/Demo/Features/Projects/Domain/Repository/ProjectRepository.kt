package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Repository

import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities.Project
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities.ProjectMember

interface ProjectRepository {
    suspend fun listProjects(): List<Project>
    suspend fun createProject(name: String, description: String?): Project
    suspend fun getProject(projectId: String): Project
    suspend fun updateProject(projectId: String, name: String?, description: String?): Project
    suspend fun deleteProject(projectId: String)
    suspend fun listMembers(projectId: String): List<ProjectMember>
    suspend fun addMember(projectId: String, userId: String, role: String)
}
