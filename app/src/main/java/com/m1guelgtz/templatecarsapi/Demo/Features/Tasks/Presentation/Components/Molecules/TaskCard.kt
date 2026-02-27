package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Presentation.Components.Molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m1guelgtz.templatecarsapi.Demo.Core.Ui.theme.*
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Entities.Task
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Presentation.Components.Atoms.Avatar
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.Entities.User

@Composable
fun TaskCard(
    task: Task,
    allUsers: List<User>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val assignedUser = allUsers.find { it.id == task.assignedTo }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Surface, RoundedCornerShape(14.dp))
            .border(1.dp, SurfaceHigh, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 11.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = task.title,
            color = OnSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.W500,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 18.sp
        )
        
        if (!task.description.isNullOrEmpty()) {
            Text(
                text = task.description,
                color = Outline,
                fontSize = 11.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (assignedUser != null) {
                Avatar(
                    initials = assignedUser.username.take(2).uppercase(),
                    color = "#7FC4FD",
                    size = 22.dp
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(SurfaceContainer, androidx.compose.foundation.shape.CircleShape)
                        .border(1.dp, OutlineVariant, androidx.compose.foundation.shape.CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("?", color = Outline, fontSize = 10.sp)
                }
            }
        }
    }
}
