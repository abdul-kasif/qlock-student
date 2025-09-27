package com.example.qlockstudentapp.utils

import android.app.Activity
import android.widget.Toast

object LockdownManager {
    fun enableLockdownMode(activity: Activity) {
        try {
            activity.startLockTask()
        } catch (t: Throwable) {
            Toast.makeText(
                activity,
                "Unable to enable lockdown mode on this device.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun disableLockdownMode(activity: Activity) {
        try {
            activity.stopLockTask()
        } catch (t: Throwable) {
            // ignore
        }
    }
}
