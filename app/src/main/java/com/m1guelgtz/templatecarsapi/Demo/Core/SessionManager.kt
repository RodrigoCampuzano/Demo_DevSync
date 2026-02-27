package com.m1guelgtz.templatecarsapi.Demo.Core

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("devsync_prefs", Context.MODE_PRIVATE)

    var userId: String?
        get() = prefs.getString("user_id", null)
        set(value) = prefs.edit().putString("user_id", value).apply()

    var token: String?
        get() = prefs.getString("auth_token", null)
        set(value) = prefs.edit().putString("auth_token", value).apply()

    var username: String?
        get() = prefs.getString("username", null)
        set(value) = prefs.edit().putString("username", value).apply()

    var email: String?
        get() = prefs.getString("email", null)
        set(value) = prefs.edit().putString("email", value).apply()

    fun isLoggedIn(): Boolean = token != null && userId != null

    fun clear() {
        prefs.edit().clear().apply()
        UserSession.clear()
    }

    fun syncToSession() {
        UserSession.userId = userId ?: ""
        UserSession.token = token ?: ""
        UserSession.username = username ?: ""
        UserSession.email = email ?: ""
    }
}
