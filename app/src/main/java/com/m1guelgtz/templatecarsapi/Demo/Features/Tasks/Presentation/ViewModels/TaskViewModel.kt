package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m1guelgtz.templatecarsapi.BuildConfig
import com.m1guelgtz.templatecarsapi.Demo.Core.UpdateEventBus
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.Task
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.TaskStatus
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.UseCases.*
import com.m1guelgtz.templatecarsapi.Demo.Core.Network.WebSocketManager
import com.m1guelgtz.templatecarsapi.Demo.Core.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TaskListState {
    object Loading : TaskListState()
    data class Success(val tasks: List<Task>) : TaskListState()
    data class Error(val message: String) : TaskListState()
}

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Repository.TaskRepository,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val webSocketManager: WebSocketManager,
    private val updateEventBus: UpdateEventBus
) : ViewModel() {

    private val _projectId = MutableStateFlow<String?>(null)
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val tasksState: StateFlow<TaskListState> = _projectId
        .filterNotNull()
        .flatMapLatest { id ->
            repository.getTasksByProjectFlow(id)
                .map<List<Task>, TaskListState> { TaskListState.Success(it) }
                .onStart { emit(TaskListState.Loading) }
                .catch { emit(TaskListState.Error(it.message ?: "Error loading tasks")) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskListState.Loading)

    init {
        observeUpdateEvents()
    }

    private fun observeUpdateEvents() {
        viewModelScope.launch {
            updateEventBus.events.collect {
                _projectId.value?.let { id ->
                    repository.syncTasksFromServer(id)
                }
            }
        }
    }

    fun setProjectId(projectId: String) {
        if (_projectId.value == projectId) return
        _projectId.value = projectId
        
        viewModelScope.launch {
            repository.syncTasksFromServer(projectId)
        }
        
        webSocketManager.connect(BuildConfig.WS_URL)
    }

    fun createTask(projectId: String, title: String, description: String?, createdBy: String) {
        viewModelScope.launch {
            try {
                val task = createTaskUseCase(projectId, title, description, createdBy)
                webSocketManager.sendUpdate(task.id, task.status.name, UserSession.userId)
                updateEventBus.emitUpdate()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTask(
        taskId: String,
        projectId: String,
        title: String?,
        description: String?,
        status: TaskStatus?,
        assignedTo: String?,
        version: Int
    ) {
        viewModelScope.launch {
            try {
                val task = updateTaskUseCase(taskId, title, description, status, assignedTo, version)
                webSocketManager.sendUpdate(task.id, task.status.name, UserSession.userId)
                updateEventBus.emitUpdate()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateStatus(taskId: String, projectId: String, status: TaskStatus, version: Int) {
        updateTask(taskId, projectId, null, null, status, null, version)
    }

    fun assignTask(taskId: String, projectId: String, userId: String) {
        updateTask(taskId, projectId, null, null, null, userId, version = 0)
    }

    fun deleteTask(taskId: String, projectId: String) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase(taskId)
                updateEventBus.emitUpdate()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.disconnect()
    }
}
