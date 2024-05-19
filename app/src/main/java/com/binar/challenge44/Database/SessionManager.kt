package com.binar.challenge44.Database

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    fun userLogin(userId: Int) {
        val editor = prefs.edit()
        editor.putInt("USER_ID", userId)
        editor.apply()
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.contains("USER_ID")
    }

    fun getUserId(): Int {
        return prefs.getInt("USER_ID", 0)
    }

    fun logout() {
        val editor = prefs.edit()
        editor.remove("USER_ID")
        editor.apply()
    }
}