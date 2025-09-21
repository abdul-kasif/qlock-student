package com.example.qlockstudentapp.utils

import android.content.Context

object AuthManager {
    fun logout(context: Context) {
        val secureStorage = SecureStorage.getInstance(context)
        secureStorage.clearToken()
    }
}