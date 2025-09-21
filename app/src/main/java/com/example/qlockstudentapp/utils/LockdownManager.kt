// utils/LockdownManager.kt
package com.example.qlockstudentapp.utils

import android.app.Activity
import android.content.Context
import android.view.WindowManager

object LockdownManager {

    fun enableLockdownMode(activity: Activity) {
        // Block screenshots
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        // Optional: Disable recent apps button (requires Device Admin)
        // Not enabled by default — needs special permissions
    }

    fun disableLockdownMode(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    // Helper to get current activity
    fun getCurrentActivity(context: Context): Activity? {
        return context as? Activity
    }
}