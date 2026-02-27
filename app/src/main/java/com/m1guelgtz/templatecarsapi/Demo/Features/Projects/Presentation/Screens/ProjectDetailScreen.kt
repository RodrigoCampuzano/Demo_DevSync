package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Presentation.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m1guelgtz.templatecarsapi.Demo.Core.Ui.theme.*
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities.Project
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Presentation.ViewModels.ProjectDetailState
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Presentation.ViewModels.ProjectViewModel
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.Task
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.TaskStatus
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Presentation.Components.Atoms.Avatar
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Presentation.Components.Molecules.TaskCard
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Presentation.ViewModels.TaskListState
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Presentation.ViewModels.TaskViewModel
import com.m1guelgtz.templatecarsapi.Demo.Core.UserSession
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Presentation.ViewModels.UserViewModel
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Presentation.ViewModels.UserListState

private data class ColumnDef(
    val id: TaskStatus,
    val label: String,
    val color: Color,
    val bg: Color,
    val headerBg: Color
)

private val COLUMNS = listOf(
    ColumnDef(TaskStatus.TODO, "TODO", TodoColor, TodoBg, TodoHeaderBg),
    ColumnDef(TaskStatus.IN_PROGRESS, "IN PROGRESS", InProgressColor, InProgressBg, InProgressHeaderBg),
    ColumnDef(TaskStatus.REVIEW, "REVIEW", ReviewColor, ReviewBg, ReviewHeaderBg),
    ColumnDef(TaskStatus.DONE, "DONE", DoneColor, DoneBg, DoneHeaderBg),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: String,
    onBackClick: () -> Unit,
    projectViewModel: ProjectViewModel = hiltViewModel(),
    taskViewModel: TaskViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val projectState by projectViewModel.projectDetailState.collectAsStateWithLifecycle()
    val tasksState by taskViewModel.tasksState.collectAsStateWithLifecycle()
    val usersState by userViewModel.usersState.collectAsStateWithLifecycle()
    
    var addTaskColumn by remember { mutableStateOf<TaskStatus?>(null) }
    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskDesc by remember { mutableStateOf("") }
    
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var showEditProjectDialog by remember { mutableStateOf(false) }

    LaunchedEffect(projectId) {
        projectViewModel.getProjectDetails(projectId)
        taskViewModel.setProjectId(projectId)
    }

    val project = (projectState as? ProjectDetailState.Success)?.project
    val allUsers = (usersState as? UserListState.Success)?.users ?: emptyList()

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(36.dp)
                        .background(SurfaceContainer, CircleShape)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Sprint Board",
                        color = OnSurfaceVariant,
                        fontSize = 11.sp,
                        letterSpacing = 0.04.sp
                    )
                    Text(
                        project?.name ?: "Loading...",
                        color = OnSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { showEditProjectDialog = true }
                    )
                }

                IconButton(
                    onClick = { /* Team */ },
                    modifier = Modifier
                        .size(36.dp)
                        .background(PrimaryContainer, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Group,
                        contentDescription = "Team",
                        tint = Primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            HorizontalDivider(color = SurfaceHigh)

            // Kanban Board
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                COLUMNS.forEach { col ->
                    val colTasks = (tasksState as? TaskListState.Success)?.tasks?.filter { it.status == col.id } ?: emptyList()

                    Column(
                        modifier = Modifier
                            .width(260.dp)
                            .fillMaxHeight()
                            .background(col.bg, RoundedCornerShape(20.dp))
                            .border(1.dp, OutlineVariant, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                    ) {
                        // Column Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(col.headerBg)
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(8.dp).background(col.color, CircleShape))
                                Text(
                                    text = col.label,
                                    color = col.color,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.W600
                                )
                            }
                            Text(
                                text = "${colTasks.size}",
                                color = col.color,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.W600,
                                modifier = Modifier
                                    .border(1.dp, OutlineVariant, RoundedCornerShape(100.dp))
                                    .padding(horizontal = 7.dp, vertical = 1.dp)
                            )
                        }

                        // Task List
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            colTasks.forEach { task ->
                                TaskCard(
                                    task = task, 
                                    allUsers = allUsers,
                                    onClick = { selectedTask = task }
                                )
                            }

                            // Add Task Inline
                            if (addTaskColumn == col.id) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(SurfaceContainer, RoundedCornerShape(14.dp))
                                        .border(1.dp, OutlineVariant, RoundedCornerShape(14.dp))
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        if (newTaskTitle.isEmpty()) {
                                            Text("Task title...", color = Outline, fontSize = 13.sp)
                                        }
                                        BasicTextField(
                                            value = newTaskTitle,
                                            onValueChange = { newTaskTitle = it },
                                            textStyle = TextStyle(color = OnSurface, fontSize = 13.sp),
                                            cursorBrush = SolidColor(Primary),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        if (newTaskDesc.isEmpty()) {
                                            Text("Brief description...", color = Outline, fontSize = 11.sp)
                                        }
                                        BasicTextField(
                                            value = newTaskDesc,
                                            onValueChange = { newTaskDesc = it },
                                            textStyle = TextStyle(color = OnSurfaceVariant, fontSize = 11.sp),
                                            cursorBrush = SolidColor(Primary),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = { addTaskColumn = null }) {
                                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                        }
                                        IconButton(onClick = {
                                            if (newTaskTitle.isNotBlank()) {
                                                taskViewModel.createTask(projectId, newTaskTitle, newTaskDesc, UserSession.userId)
                                                newTaskTitle = ""
                                                newTaskDesc = ""
                                                addTaskColumn = null
                                            }
                                        }) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = col.color, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, OutlineVariant, RoundedCornerShape(12.dp))
                                        .clickable { addTaskColumn = col.id }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(Icons.Default.Add, contentDescription = null, tint = Outline, modifier = Modifier.size(14.dp))
                                        Text("Add Task", color = Outline, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Edit Project Dialog
        if (showEditProjectDialog && project != null) {
            var editName by remember { mutableStateOf(project.name) }
            var editDesc by remember { mutableStateOf(project.description ?: "") }

            AlertDialog(
                onDismissRequest = { showEditProjectDialog = false },
                containerColor = SurfaceContainer,
                title = { Text("Edit Project", color = OnSurface) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = editDesc,
                            onValueChange = { editDesc = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        projectViewModel.updateProject(projectId, editName, editDesc)
                        showEditProjectDialog = false
                    }) {
                        Text("Save", color = Primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditProjectDialog = false }) {
                        Text("Cancel", color = OnSurfaceVariant)
                    }
                }
            )
        }

        // Task Detail Selection UI
        if (selectedTask != null) {
            val task = selectedTask!!
            
            // Move state up so confirmButton can access it
            var editTitle by remember(task.id) { mutableStateOf(task.title) }
            var editDescription by remember(task.id) { mutableStateOf(task.description ?: "") }
            var currentStatus by remember(task.id) { mutableStateOf(task.status) }
            var currentAssignedTo by remember(task.id) { mutableStateOf(task.assignedTo) }

            AlertDialog(
                onDismissRequest = { selectedTask = null },
                containerColor = SurfaceContainer,
                title = { Text("Edit Task", color = OnSurface) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = editTitle,
                            onValueChange = { editTitle = it },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = editDescription,
                            onValueChange = { editDescription = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        // Status Selector
                        Column {
                            Text("STATUS", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                            ScrollableTabRow(
                                selectedTabIndex = COLUMNS.indexOfFirst { it.id == currentStatus },
                                edgePadding = 0.dp,
                                containerColor = Color.Transparent,
                                divider = {}
                            ) {
                                COLUMNS.forEach { col ->
                                    Tab(
                                        selected = currentStatus == col.id,
                                        onClick = { currentStatus = col.id },
                                        text = { Text(col.label, fontSize = 10.sp) }
                                    )
                                }
                            }
                        }

                        // Assignee Selector
                        Column {
                            Text("ASSIGN TO", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyColumn(modifier = Modifier.height(150.dp)) {
                                items(allUsers) { user ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { currentAssignedTo = user.id }
                                            .padding(vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Avatar(initials = user.username.take(2).uppercase(), color = "#D0BCFF", size = 24.dp)
                                        Text(user.username, color = OnSurface)
                                        if (currentAssignedTo == user.id) {
                                            Spacer(modifier = Modifier.weight(1f))
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { 
                        taskViewModel.updateTask(
                            taskId = task.id,
                            projectId = projectId,
                            title = editTitle,
                            description = editDescription,
                            status = currentStatus,
                            assignedTo = currentAssignedTo,
                            version = task.version
                        )
                        selectedTask = null 
                    }) {
                        Text("Save Changes", color = Primary)
                    }
                },
                dismissButton = {
                    IconButton(onClick = { 
                        taskViewModel.deleteTask(task.id, projectId)
                        selectedTask = null
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorColor)
                    }
                }
            )
        }
    }
}
