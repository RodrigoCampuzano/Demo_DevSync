package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m1guelgtz.templatecarsapi.BuildConfig
import com.m1guelgtz.templatecarsapi.Demo.Core.UpdateEventBus
import com.m1guelgtz.templatecarsapi.Demo.Core.Network.WebSocketManager
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities.Project
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.UseCases.*
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.Task
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.TaskStatus
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.UseCases.GetTasksByProjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProjectListState {
    object Loading : ProjectListState()
    data class Success(
        val projects: List<Project>,
        val totalTasks: Int,
        val inProgressTasks: Int,
        val projectTaskCounts: Map<String, Pair<Int, Int>> // projectId -> (done, total)
    ) : ProjectListState()
    data class Error(val message: String) : ProjectListState()
}

sealed class ProjectDetailState {
    object Idle : ProjectDetailState()
    object Loading : ProjectDetailState()
    data class Success(val project: Project) : ProjectDetailState()
    data class Error(val message: String) : ProjectDetailState()
}

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val listProjectsUseCase: ListProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val getProjectUseCase: GetProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getTasksByProjectUseCase: GetTasksByProjectUseCase,
    private val updateEventBus: UpdateEventBus,
    private val webSocketManager: WebSocketManager
) : ViewModel() {

    private val _projectsState = MutableStateFlow<ProjectListState>(ProjectListState.Loading)
    val projectsState: StateFlow<ProjectListState> = _projectsState.asStateFlow()

    private val _projectDetailState = MutableStateFlow<ProjectDetailState>(ProjectDetailState.Idle)
    val projectDetailState: StateFlow<ProjectDetailState> = _projectDetailState.asStateFlow()

    init {
        loadProjects()
        observeUpdates()
        // Ensure socket is connected to receive background updates for percentages
        webSocketManager.connect(BuildConfig.WS_URL)
    }

    private fun observeUpdates() {
        viewModelScope.launch {
            updateEventBus.events.collect {
                loadProjects(silent = true)
            }
        }
    }

    fun loadProjects(silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) _projectsState.value = ProjectListState.Loading
            try {
                val projects = listProjectsUseCase()
                var totalTasks = 0
                var inProgress = 0
                val projectTaskCounts = mutableMapOf<String, Pair<Int, Int>>()

                projects.forEach { project ->
                    try {
                        val tasks = getTasksByProjectUseCase(project.id)
                        val done = tasks.count { it.status == TaskStatus.DONE }
                        val currentInProgress = tasks.count { it.status == TaskStatus.IN_PROGRESS }
                        
                        totalTasks += tasks.size
                        inProgress += currentInProgress
                        projectTaskCounts[project.id] = Pair(done, tasks.size)
                    } catch (e: Exception) {
                        projectTaskCounts[project.id] = Pair(0, 0)
                    }
                }

                _projectsState.value = ProjectListState.Success(
                    projects = projects,
                    totalTasks = totalTasks,
                    inProgressTasks = inProgress,
                    projectTaskCounts = projectTaskCounts
                )
            } catch (e: Exception) {
                if (!silent) _projectsState.value = ProjectListState.Error(e.message ?: "Failed to load projects")
            }
        }
    }

    fun createProject(name: String, description: String?) {
        viewModelScope.launch {
            try {
                createProjectUseCase(name, description)
                loadProjects(silent = true)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun getProjectDetails(projectId: String) {
        viewModelScope.launch {
            _projectDetailState.value = ProjectDetailState.Loading
            try {
                val project = getProjectUseCase(projectId)
                _projectDetailState.value = ProjectDetailState.Success(project)
            } catch (e: Exception) {
                _projectDetailState.value = ProjectDetailState.Error(e.message ?: "Failed to load project details")
            }
        }
    }

    fun updateProject(projectId: String, name: String?, description: String?) {
        viewModelScope.launch {
            try {
                updateProjectUseCase(projectId, name, description)
                loadProjects(silent = true)
                getProjectDetails(projectId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            try {
                deleteProjectUseCase(projectId)
                loadProjects(silent = true)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
