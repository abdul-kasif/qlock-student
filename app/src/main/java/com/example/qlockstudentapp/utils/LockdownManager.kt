// utils/LockdownManager.kt
package com.example.qlockstudentapp.utils

import android.app.Activity
import android.os.Build
import android.widget.Toast

object LockdownManager {
    fun enableLockdownMode(activity: Activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // startLockTask will pin the activity
                activity.startLockTask()
            }
        } catch (t: Throwable) {
            // best-effort; show user feedback
            Toast.makeText(activity, "Unable to enable lockdown mode on this device.", Toast.LENGTH_SHORT).show()
        }
    }

    fun disableLockdownMode(activity: Activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.stopLockTask()
            }
        } catch (t: Throwable) {
            // ignore
        }
    }
}
