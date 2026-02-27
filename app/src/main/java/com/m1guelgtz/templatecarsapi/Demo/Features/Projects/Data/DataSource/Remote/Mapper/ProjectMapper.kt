package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Data.DataSource.Remote.Mapper

import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Data.DataSource.Remote.Model.ProjectDto
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Data.DataSource.Remote.Model.ProjectMemberDto
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities.Project
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities.ProjectMember
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities.ProjectRole

fun ProjectDto.toDomain(): Project {
    return Project(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt
    )
}

fun ProjectMemberDto.toDomain(): ProjectMember {
    return ProjectMember(
        userId = this.userId,
        username = this.username,
        role = try { ProjectRole.valueOf(this.role) } catch (e: Exception) { ProjectRole.DEVELOPER },
        joinedAt = this.joinedAt
    )
}
