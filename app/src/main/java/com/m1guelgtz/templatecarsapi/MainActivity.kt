package com.m1guelgtz.templatecarsapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.m1guelgtz.templatecarsapi.Demo.Core.SessionManager
import com.m1guelgtz.templatecarsapi.Demo.Core.Ui.theme.TemplateCarsAPITheme
import com.m1guelgtz.templatecarsapi.Demo.Core.rutes.AppNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TemplateCarsAPITheme {
                AppNavHost(
                    navController = rememberNavController(),
                    sessionManager = sessionManager
                )
            }
        }
    }
}
