package com.m1guelgtz.templatecarsapi.Demo.Core.Ui.theme

import androidx.compose.ui.graphics.Color

// DevSync Brand Colors - Precise match to screenshots
val Background = Color(0xFF141218)
val Surface = Color(0xFF1D1B20)
val SurfaceContainer = Color(0xFF211F26)
val SurfaceHigh = Color(0xFF2B2930)
val SurfaceHighest = Color(0xFF36343B)
val OnSurface = Color(0xFFE6E1E5)
val OnSurfaceVariant = Color(0xFFCAC4D0)
val Outline = Color(0xFF938F99)
val OutlineVariant = Color(0xFF49454F)
val Primary = Color(0xFFD0BCFF)
val OnPrimary = Color(0xFF381E72)
val PrimaryContainer = Color(0xFF4F378B)
val OnPrimaryContainer = Color(0xFFEADDFF)
val ErrorColor = Color(0xFFF2B8B5)
val ErrorContainer = Color(0xFF8C1D18)

// Kanban Column accent colors
val TodoColor = Color(0xFFCAC4D0)
val InProgressColor = Color(0xFF7FC4FD)
val ReviewColor = Color(0xFFFFBD59)
val DoneColor = Color(0xFF81C784)

// Kanban Column background colors
val TodoBg = Color(0xFF1D1B20)
val InProgressBg = Color(0xFF091929)
val ReviewBg = Color(0xFF1A1100)
val DoneBg = Color(0xFF091409)

// Kanban Column header backgrounds
val TodoHeaderBg = Color(0xFF2B2930)
val InProgressHeaderBg = Color(0xFF0D2137)
val ReviewHeaderBg = Color(0xFF2C1E00)
val DoneHeaderBg = Color(0xFF0D2010)

// Project card accent colors
data class ProjectColorScheme(val bg: Color, val accent: Color)

val PROJECT_COLORS = listOf(
    ProjectColorScheme(Color(0xFF1A237E), Color(0xFF82B1FF)),
    ProjectColorScheme(Color(0xFF1B5E20), Color(0xFF69F0AE)),
    ProjectColorScheme(Color(0xFF4A148C), Color(0xFFEA80FC)),
    ProjectColorScheme(Color(0xFFBF360C), Color(0xFFFF6E40)),
    ProjectColorScheme(Color(0xFF006064), Color(0xFF84FFFF)),
)
