// ui/screens/quiz/QuizLockdownActivity.kt
package com.example.qlockstudentapp.ui.screens.quiz

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
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

        // Block screenshots & recording
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        accessCode = intent.getStringExtra(EXTRA_ACCESS_CODE) ?: ""

        // Enable pinned mode (best-effort)
        LockdownManager.enableLockdownMode(this)

        setContent {
            QLockStudentAppTheme {
                // Pass the same viewModel instance into the composable
                QuizLockdownScreenHost(accessCode = accessCode, quizViewModel = quizViewModel,
                    onQuizSubmit = { score ->
                        // prevent double handling
                        if (hasSubmitted) return@QuizLockdownScreenHost
                        hasSubmitted = true
                        LockdownManager.disableLockdownMode(this)
                        // Launch result screen (Activity) and finish lockdown
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

    fun onBackPressedDispatcher() {
        // Block back navigation while lockdown is active
        Toast.makeText(this, "You cannot exit during the quiz.", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        // triggered when app loses focus (e.g., Home/Recents). Auto-submit once.
        if (!hasSubmitted && quizViewModel.quiz != null) {
            // call submitQuiz; backend ensures single submission
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
