// ui/screens/quiz/QuizResultActivity.kt
package com.example.campqstudent.ui.screens.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.campqstudent.MainActivity
import com.example.campqstudent.ui.theme.CampQAppTheme
import com.example.campqstudent.utils.LockdownManager

class QuizResultActivity : ComponentActivity() {

    companion object {
        private const val EXTRA_SCORE = "extra_score"

        fun start(context: Context, score: Int) {
            val intent = Intent(context, QuizResultActivity::class.java).apply {
                putExtra(EXTRA_SCORE, score)
                // Do NOT add NEW_TASK — keep in same task
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val score = intent.getIntExtra(EXTRA_SCORE, 0)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            CampQAppTheme {
                QuizResultScreen(
                    onBackToDashboard = {
                        // ✅ Disable lockdown HERE, then go to Dashboard
                        LockdownManager.disableLockdownMode(this)
                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("goTo", "dashboard")
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                        startActivity(intent)
                        finish()
                    },
                    score = score
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Fallback: disable lockdown if activity is destroyed unexpectedly
        LockdownManager.disableLockdownMode(this)
    }
}