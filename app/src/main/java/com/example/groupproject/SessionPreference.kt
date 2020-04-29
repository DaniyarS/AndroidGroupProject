package com.example.groupproject

import android.content.Context

class SessionPreference(context: Context) {

    val PREFERENCE = "myPreference"
    val LOGIN_COUNT = "LoginCount"
    val USERNAME = "Username"
    val SESSIONID = "SessionId"
    val REALSESSIONID = "RealSessionId"
    val STAR_STATE = "StarState"

    val preference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)

    fun getLoginCount(): Int {
        return preference.getInt(LOGIN_COUNT, 0)
    }

    fun getUsername(): String? {
        return preference.getString(USERNAME, "Null user")
    }

    fun setUsername(username: String) {
        val editor = preference.edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }

    fun getSessionId(): String? {
        return preference.getString(SESSIONID, "Null session")
    }

    fun setSessionId(sessionId: String) {
        val editor = preference.edit()
        editor.putString(SESSIONID, sessionId)
        editor.apply()
    }

    fun getRealSessionId(): String? {
        return preference.getString(REALSESSIONID, "Null session")
    }

    fun setRealSessionId(sessionId: String) {
        val editor = preference.edit()
        editor.putString(REALSESSIONID, sessionId)
        editor.apply()
    }

    fun setLoginCount(count: Int) {
        val editor = preference.edit()
        editor.putInt(LOGIN_COUNT, count)
        editor.apply()
    }
}