package com.m1guelgtz.templatecarsapi.Demo.Core.rutes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.m1guelgtz.templatecarsapi.Demo.Core.SessionManager
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Presentation.Screens.LoginScreen
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Presentation.Screens.ProjectDetailScreen
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Presentation.Screens.ProjectListScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    sessionManager: SessionManager
) {
    val startDest = if (sessionManager.isLoggedIn()) RutaProjectList else RutaLogin

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable<RutaLogin> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(RutaProjectList) {
                        popUpTo(RutaLogin) { inclusive = true }
                    }
                }
            )
        }

        composable<RutaProjectList> {
            ProjectListScreen(
                onProjectClick = { projectId ->
                    navController.navigate(RutaProjectDetail(projectId))
                },
                onLogout = {
                    navController.navigate(RutaLogin) {
                        popUpTo(RutaProjectList) { inclusive = true }
                    }
                },
                sessionManager = sessionManager
            )
        }

        composable<RutaProjectDetail> { backStackEntry ->
            val route: RutaProjectDetail = backStackEntry.toRoute()
            ProjectDetailScreen(
                projectId = route.projectId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
