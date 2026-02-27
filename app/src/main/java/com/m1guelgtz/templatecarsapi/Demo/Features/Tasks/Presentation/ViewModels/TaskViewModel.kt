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

data class TaskUiState(
    val addTaskColumn: TaskStatus? = null,
    val newTaskTitle: String = "",
    val newTaskDesc: String = "",
    val selectedTask: Task? = null,
    val editTitle: String = "",
    val editDescription: String = "",
    val currentStatus: TaskStatus = TaskStatus.TODO,
    val currentAssignedTo: String? = null
)

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
                .map { tasks -> TaskListState.Success(tasks) as TaskListState }
                .onStart { emit(TaskListState.Loading) }
                .catch { emit(TaskListState.Error(it.message ?: "Error loading tasks")) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskListState.Loading)

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

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

    fun onAddTaskColumnChanged(column: TaskStatus?) {
        _uiState.update { it.copy(addTaskColumn = column, newTaskTitle = "", newTaskDesc = "") }
    }

    fun onNewTitleChanged(title: String) {
        _uiState.update { it.copy(newTaskTitle = title) }
    }

    fun onNewDescChanged(desc: String) {
        _uiState.update { it.copy(newTaskDesc = desc) }
    }

    fun onSelectedTaskChanged(task: Task?) {
        _uiState.update { it.copy(
            selectedTask = task,
            editTitle = task?.title ?: "",
            editDescription = task?.description ?: "",
            currentStatus = task?.status ?: TaskStatus.TODO,
            currentAssignedTo = task?.assignedTo
        ) }
    }

    fun onEditTitleChanged(title: String) {
        _uiState.update { it.copy(editTitle = title) }
    }

    fun onEditDescriptionChanged(desc: String) {
        _uiState.update { it.copy(editDescription = desc) }
    }

    fun onCurrentStatusChanged(status: TaskStatus) {
        _uiState.update { it.copy(currentStatus = status) }
    }

    fun onCurrentAssignedToChanged(userId: String?) {
        _uiState.update { it.copy(currentAssignedTo = userId) }
    }

    fun setProjectId(projectId: String) {
        if (_projectId.value == projectId) return
        _projectId.value = projectId
        
        viewModelScope.launch {
            repository.syncTasksFromServer(projectId)
        }
        
        webSocketManager.connect(BuildConfig.WS_URL)
    }

    fun createTask(projectId: String) {
        val state = _uiState.value
        viewModelScope.launch {
            try {
                val task = createTaskUseCase(projectId, state.newTaskTitle, state.newTaskDesc, UserSession.userId)
                _uiState.update { it.copy(addTaskColumn = null, newTaskTitle = "", newTaskDesc = "") }
                webSocketManager.sendUpdate(task.id, task.status.name, UserSession.userId)
                updateEventBus.emitUpdate()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTaskFromDialog(projectId: String) {
        val state = _uiState.value
        val task = state.selectedTask ?: return
        viewModelScope.launch {
            try {
                val updatedTask = updateTaskUseCase(
                    task.id, 
                    state.editTitle, 
                    state.editDescription, 
                    state.currentStatus, 
                    state.currentAssignedTo, 
                    task.version
                )
                webSocketManager.sendUpdate(updatedTask.id, updatedTask.status.name, UserSession.userId)
                _uiState.update { it.copy(selectedTask = null) }
                updateEventBus.emitUpdate()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTask(projectId: String) {
        val task = _uiState.value.selectedTask ?: return
        viewModelScope.launch {
            try {
                deleteTaskUseCase(task.id)
                _uiState.update { it.copy(selectedTask = null) }
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
