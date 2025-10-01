package com.example.qlockstudentapp.ui.screens.quiz

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

class QuizPinningLauncherActivity : ComponentActivity() {

    companion object {
        fun start(context: Context, accessCode: String) {
            val intent = Intent(context, QuizPinningLauncherActivity::class.java)
            intent.putExtra("accessCode", accessCode)
            context.startActivity(intent)
        }
    }

    private lateinit var accessCode: String
    private var quizLaunched = false
    private var declinedHandled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accessCode = intent.getStringExtra("accessCode") ?: return finish()

        // Minimal Compose content to avoid black/white flash
        setContent {
            androidx.compose.material3.Surface(
                modifier = Modifier.fillMaxSize(),
                color = androidx.compose.material3.MaterialTheme.colorScheme.background
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    androidx.compose.material3.Text(
                        text = "Preparing Quiz...",
                        modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
                        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }

        // Request pinning (shows system dialog)
        startLockTask()

        // Start polling lock task state
        pollLockTaskState()
    }

    private fun pollLockTaskState() {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        window.decorView.postDelayed({
            if (!quizLaunched && !declinedHandled) {
                when {
                    am.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE -> {
                        // ✅ User accepted → launch quiz
                        quizLaunched = true
                        QuizLockdownActivity.launch(this, accessCode)
                    }
                    !hasWindowFocus() -> {
                        // ❌ User declined ("No Thanks")
                        declinedHandled = true
                        Toast.makeText(this, "Screen pinning declined", Toast.LENGTH_SHORT).show()
                        finish() // or navigate to DashboardActivity.start(this)
                    }
                    else -> pollLockTaskState()
                }
            }
        }, 300) // check every 300ms
    }

    override fun onPause() {
        super.onPause()
        if (quizLaunched) {
            finish() // safe to close launcher after launching quiz
        }
    }
}
