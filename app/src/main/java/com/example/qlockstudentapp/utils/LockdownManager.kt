package com.example.qlockstudentapp.utils

import android.app.Activity
import android.os.Build
import android.view.WindowManager

object LockdownManager {

    /** Enable secure flags to block screenshots */
    fun enableLockdownMode(activity: Activity) {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    /** Remove secure flags */
    fun disableLockdownMode(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
}
