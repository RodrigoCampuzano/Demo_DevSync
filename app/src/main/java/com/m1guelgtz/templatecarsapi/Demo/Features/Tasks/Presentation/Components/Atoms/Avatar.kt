package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Presentation.Components.Atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Avatar(
    initials: String,
    color: String,
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    val parsedColor = try {
        Color(android.graphics.Color.parseColor(color))
    } catch (e: Exception) {
        Color.Gray
    }

    Box(
        modifier = modifier
            .size(size)
            .background(
                color = parsedColor.copy(alpha = 0.2f),
                shape = CircleShape
            )
            .border(
                width = 1.5.dp,
                color = parsedColor.copy(alpha = 0.5f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = parsedColor,
            fontSize = (size.value * 0.38f).sp,
            fontWeight = FontWeight.W600,
        )
    }
}
