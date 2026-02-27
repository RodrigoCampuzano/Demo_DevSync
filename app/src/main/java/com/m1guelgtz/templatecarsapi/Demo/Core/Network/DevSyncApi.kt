package com.m1guelgtz.templatecarsapi.Demo.Core.Network

import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Data.DataSource.Remote.Model.*
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Data.DataSource.Remote.Model.*
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Data.DataSource.Remote.Model.*
import retrofit2.http.*

interface DevSyncApi {
    // Auth
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    // Projects
    @GET("api/v1/projects/")
    suspend fun listProjects(): List<ProjectDto>

    @POST("api/v1/projects/")
    suspend fun createProject(@Body request: CreateProjectRequest): ProjectDto

    @GET("api/v1/projects/{project_id}")
    suspend fun getProject(@Path("project_id") projectId: String): ProjectDto

    @PUT("api/v1/projects/{project_id}")
    suspend fun updateProject(@Path("project_id") id: String, @Body request: ProjectUpdate): ProjectDto

    @DELETE("api/v1/projects/{project_id}")
    suspend fun deleteProject(@Path("project_id") projectId: String)

    @GET("api/v1/projects/{project_id}/members")
    suspend fun listMembers(@Path("project_id") projectId: String): List<ProjectMemberDto>

    @POST("api/v1/projects/{project_id}/members")
    suspend fun addMember(@Path("project_id") projectId: String, @Body request: AddMemberRequest)

    // Tasks
    @GET("api/v1/tasks/project/{project_id}")
    suspend fun getTasksByProject(@Path("project_id") projectId: String): List<TaskDto>

    @POST("api/v1/tasks/")
    suspend fun createTask(@Body request: CreateTaskRequest): TaskDto

    @GET("api/v1/tasks/{task_id}")
    suspend fun getTask(@Path("task_id") taskId: String): TaskDto

    @PUT("api/v1/tasks/{task_id}")
    suspend fun updateTask(@Path("task_id") id: String, @Body request: TaskUpdate): TaskDto

    @DELETE("api/v1/tasks/{task_id}")
    suspend fun deleteTask(@Path("task_id") taskId: String)

    @PATCH("api/v1/tasks/{task_id}/status")
    suspend fun updateTaskStatus(@Path("task_id") taskId: String, @Body request: UpdateTaskStatusRequest): TaskDto

    @PATCH("api/v1/tasks/{task_id}/assign")
    suspend fun assignTask(@Path("task_id") taskId: String, @Body request: AssignTaskRequest): TaskDto

    // Users
    @GET("api/v1/users/")
    suspend fun listUsers(): List<UserDto>

    @POST("api/v1/users/")
    suspend fun createUser(@Body request: CreateUserRequest): UserDto

    @GET("api/v1/users/{user_id}")
    suspend fun getUser(@Path("user_id") userId: String): UserDto

    @DELETE("api/v1/users/{user_id}")
    suspend fun deleteUser(@Path("user_id") userId: String)
}
