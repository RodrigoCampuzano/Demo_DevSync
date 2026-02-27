package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Presentation.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m1guelgtz.templatecarsapi.Demo.Core.Ui.theme.*
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Entities.Project
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Presentation.ViewModels.ProjectListState
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Presentation.ViewModels.ProjectViewModel
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Presentation.Components.Atoms.Avatar
import com.m1guelgtz.templatecarsapi.Demo.Core.UserSession
import com.m1guelgtz.templatecarsapi.Demo.Core.SessionManager

@Composable
fun ProjectListScreen(
    onProjectClick: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: ProjectViewModel = hiltViewModel(),
    sessionManager: SessionManager
) {
    val state by viewModel.projectsState.collectAsStateWithLifecycle()
    var showCreateSheet by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    
    var projectToDelete by remember { mutableStateOf<Project?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WELCOME BACK",
                        color = OnSurfaceVariant,
                        fontSize = 12.sp,
                        letterSpacing = 0.05.sp
                    )
                    Text(
                        text = "My Projects",
                        color = OnSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W500,
                        letterSpacing = (-0.3).sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Avatar(
                        initials = if (UserSession.username.isNotEmpty()) UserSession.username.take(2).uppercase() else "AK",
                        color = "#D0BCFF",
                        size = 36.dp
                    )
                    
                    IconButton(
                        onClick = { 
                            sessionManager.clear()
                            onLogout()
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .background(SurfaceContainer, CircleShape)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Project List Content
            when (val currentState = state) {
                is ProjectListState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Primary)
                    }
                }
                is ProjectListState.Success -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatChip("Projects", "${currentState.projects.size}", modifier = Modifier.weight(1f))
                        StatChip("Total Tasks", "${currentState.totalTasks}", modifier = Modifier.weight(1f))
                        StatChip("In Progress", "${currentState.inProgressTasks}", accent = true, modifier = Modifier.weight(1f))
                    }

                    if (currentState.projects.isEmpty()) {
                        EmptyProjectsView()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(currentState.projects) { index, project ->
                                val colorScheme = PROJECT_COLORS[index % PROJECT_COLORS.size]
                                val taskCountPair = currentState.projectTaskCounts[project.id] ?: Pair(0, 0)
                                
                                ProjectCard(
                                    project = project,
                                    colorScheme = colorScheme,
                                    doneTasks = taskCountPair.first,
                                    totalTasks = taskCountPair.second,
                                    onClick = { onProjectClick(project.id) },
                                    onLongClick = { projectToDelete = project }
                                )
                            }
                            item { Spacer(modifier = Modifier.height(100.dp)) }
                        }
                    }
                }
                is ProjectListState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(currentState.message, color = ErrorColor)
                    }
                }
            }
        }

        // FAB
        if (!showCreateSheet) {
            FloatingActionButton(
                onClick = { showCreateSheet = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp),
                containerColor = Primary,
                contentColor = OnPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Project")
            }
        }

        if (showCreateSheet) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { showCreateSheet = false }
            )
            
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        SurfaceContainer,
                        RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                    )
                    .padding(bottom = 32.dp)
            ) {
                // Handle
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(4.dp)
                            .background(OutlineVariant, RoundedCornerShape(2.dp))
                    )
                }

                Text(
                    "New Project",
                    color = OnSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                )

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Project Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newDesc,
                        onValueChange = { newDesc = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            if (newName.isNotBlank()) {
                                viewModel.createProject(newName, newDesc)
                                newName = ""
                                newDesc = ""
                                showCreateSheet = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Create Project")
                    }
                }
            }
        }

        // Delete Project Confirmation
        if (projectToDelete != null) {
            AlertDialog(
                onDismissRequest = { projectToDelete = null },
                containerColor = SurfaceContainer,
                title = { Text("Delete Project?", color = OnSurface) },
                text = { Text("This will permanently remove '${projectToDelete?.name}' and all its tasks. This action cannot be undone.", color = OnSurfaceVariant) },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteProject(projectToDelete!!.id)
                        projectToDelete = null
                    }) {
                        Text("Delete", color = ErrorColor)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { projectToDelete = null }) {
                        Text("Cancel", color = OnSurfaceVariant)
                    }
                }
            )
        }
    }
}

@Composable
private fun StatChip(
    label: String,
    value: String,
    accent: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                if (accent) Color(0xFF1A1040) else Surface,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                if (accent) PrimaryContainer else SurfaceHigh,
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = value,
            color = if (accent) Primary else OnSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.W500
        )
        Text(
            text = label,
            color = Outline,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun EmptyProjectsView() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            Icons.Default.Folder,
            contentDescription = null,
            tint = OnSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(40.dp)
        )
        Text("No projects yet. Tap + to create one.", color = OnSurfaceVariant)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectCard(
    project: Project,
    colorScheme: ProjectColorScheme,
    doneTasks: Int,
    totalTasks: Int,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val progress = if (totalTasks > 0) doneTasks.toFloat() / totalTasks else 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface, RoundedCornerShape(20.dp))
            .border(1.dp, SurfaceHigh, RoundedCornerShape(20.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(colorScheme.bg, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Folder, contentDescription = null, tint = colorScheme.accent)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.name, 
                    color = OnSurface, 
                    fontSize = 15.sp, 
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = project.description ?: "",
                    color = Outline,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight, 
                contentDescription = null, 
                tint = OutlineVariant,
                modifier = Modifier.size(18.dp).padding(top = 2.dp)
            )
        }

        // Progress
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("$doneTasks / $totalTasks tasks done", color = OnSurfaceVariant, fontSize = 11.sp)
                Text(
                    "${(progress * 100).toInt()}%",
                    color = colorScheme.accent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W500
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(SurfaceHigh, RoundedCornerShape(2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .background(colorScheme.accent, RoundedCornerShape(2.dp))
                )
            }
        }

        Text(
            "Members managed in sprint",
            color = Outline,
            fontSize = 11.sp
        )
    }
}
