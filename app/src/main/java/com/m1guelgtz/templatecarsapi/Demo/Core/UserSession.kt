package com.m1guelgtz.templatecarsapi.Demo.Core

object UserSession {
    var userId: String = ""
    var token: String = ""
    var username: String = ""
    var email: String = ""

    fun clear() {
        userId = ""
        token = ""
        username = ""
        email = ""
    }
}
