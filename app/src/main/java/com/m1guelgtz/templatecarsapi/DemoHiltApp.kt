package com.m1guelgtz.templatecarsapi

import android.app.Application
import com.m1guelgtz.templatecarsapi.Demo.Core.SessionManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DemoHiltApp : Application() {
    
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate() {
        super.onCreate()
        // Sync persistent data to memory session on app start
        if (sessionManager.isLoggedIn()) {
            sessionManager.syncToSession()
        }
    }
}
