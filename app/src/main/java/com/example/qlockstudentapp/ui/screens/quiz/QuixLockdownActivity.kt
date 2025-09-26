package com.example.qlockstudentapp.ui.screens.quiz

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.qlockstudentapp.ui.theme.QLockStudentAppTheme
import com.example.qlockstudentapp.utils.LockdownManager
import com.example.qlockstudentapp.viewmodel.QuizViewModel

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
        super.onCreate(savedInstanceState)

        // 🚫 Block screenshots & screen recording
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

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
            QLockStudentAppTheme {
                QuizLockdownScreenHost(
                    accessCode = accessCode,
                    quizViewModel = quizViewModel,
                    onQuizSubmit = { score ->
                        if (hasSubmitted) return@QuizLockdownScreenHost
                        hasSubmitted = true
                        LockdownManager.disableLockdownMode(this)
                        QuizResultActivity.start(this, score)
                        finish()
                    },
                    onQuizError = { error ->
                        if (hasSubmitted) return@QuizLockdownScreenHost
                        hasSubmitted = true
                        LockdownManager.disableLockdownMode(this)
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
                    LockdownManager.disableLockdownMode(this)
                    QuizResultActivity.start(this, response.score)
                    finish()
                },
                onError = { error ->
                    hasSubmitted = true
                    LockdownManager.disableLockdownMode(this)
                    Toast.makeText(this, "Auto-submit failed: $error", Toast.LENGTH_LONG).show()
                    finish()
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LockdownManager.disableLockdownMode(this)
    }
}
