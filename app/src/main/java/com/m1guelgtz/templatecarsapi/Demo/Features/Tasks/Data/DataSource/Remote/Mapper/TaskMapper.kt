package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Data.DataSource.Remote.Mapper

import com.m1guelgtz.templatecarsapi.Demo.Core.Data.Local.TaskEntity
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Data.DataSource.Remote.Model.TaskDto
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.Task
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.TaskStatus

fun TaskDto.toDomain(): Task {
    return Task(
        id = this.id,
        projectId = this.projectId,
        title = this.title,
        description = this.description,
        status = try { TaskStatus.valueOf(this.status) } catch (e: Exception) { TaskStatus.TODO },
        assignedTo = this.assignedTo,
        createdBy = this.createdBy,
        version = this.version,
        updatedAt = this.updatedAt,
        createdAt = this.createdAt
    )
}

fun TaskDto.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        task_id = this.taskId, // Friendly ID support
        projectId = this.projectId,
        title = this.title,
        description = this.description,
        status = this.status,
        assignedTo = this.assignedTo,
        createdBy = this.createdBy,
        version = this.version,
        updatedAt = this.updatedAt,
        createdAt = this.createdAt
    )
}

fun TaskEntity.toDomain(): Task {
    return Task(
        id = this.id,
        projectId = this.projectId,
        title = this.title,
        description = this.description,
        status = try { TaskStatus.valueOf(this.status) } catch (e: Exception) { TaskStatus.TODO },
        assignedTo = this.assignedTo,
        createdBy = this.createdBy,
        version = this.version,
        updatedAt = this.updatedAt,
        createdAt = this.createdAt
    )
}
