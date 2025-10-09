package com.example.campqstudent.ui.screens.quiz

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.campqstudent.ui.screens.result.QuizResultActivity
import com.example.campqstudent.ui.theme.CampQAppTheme
import com.example.campqstudent.utils.LockdownManager
import com.example.campqstudent.viewmodel.QuizViewModel

class QuizLockdownActivity : ComponentActivity() {

    companion object {
        private const val EXTRA_ACCESS_CODE = "extra_access_code"

        fun launch(context: Context, accessCode: String) {
            val intent = Intent(context, QuizLockdownActivity::class.java).apply {
                putExtra(EXTRA_ACCESS_CODE, accessCode)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val quizViewModel: QuizViewModel by viewModels()
    private lateinit var accessCode: String
    private var hasSubmitted = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        super.onCreate(savedInstanceState)

        accessCode = intent.getStringExtra(EXTRA_ACCESS_CODE) ?: ""

        // 🚫 Force Lockdown Mode (shows "Start / No Thanks" dialog)
        LockdownManager.enableLockdownMode(this)

        // 🚫 Block back press (Android 12 and below)
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Toast.makeText(
                        this@QuizLockdownActivity,
                        "Back is disabled during the quiz.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        // 🚫 Block back gestures on Android 13+ (API 33)
        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                Toast.makeText(
                    this,
                    "Back is disabled during the quiz.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        setContent {
            CampQAppTheme {
                QuizLockdownScreenHost(
                    accessCode = accessCode,
                    quizViewModel = quizViewModel,
                    onQuizSubmit = { score ->
                        if (hasSubmitted) return@QuizLockdownScreenHost
                        hasSubmitted = true
                        QuizResultActivity.start(this, score)
                        finish()
                    },
                    onQuizError = { error ->
                        if (hasSubmitted) return@QuizLockdownScreenHost
                        hasSubmitted = true
                        Toast.makeText(this, "Submit failed: $error", Toast.LENGTH_LONG).show()
                        finish()
                    }
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Auto-submit if student leaves quiz before finishing
        if (!hasSubmitted && quizViewModel.quiz != null) {
            quizViewModel.submitQuiz(
                onSuccess = { response ->
                    hasSubmitted = true
                    QuizResultActivity.start(this, response.score)
                    finish()
                },
                onError = { error ->
                    hasSubmitted = true
                    Toast.makeText(this, "Auto-submit failed: $error", Toast.LENGTH_LONG).show()
                    finish()
                }
            )
        }
    }

    @SuppressLint("ServiceCast")
    override fun onResume() {
        super.onResume()
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
            am.lockTaskModeState == ActivityManager.LOCK_TASK_MODE_NONE
        ) {
            // User did not accept pinning → return to dashboard
            Toast.makeText(this, "Screen pinning not enabled. Returning to dashboard.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
