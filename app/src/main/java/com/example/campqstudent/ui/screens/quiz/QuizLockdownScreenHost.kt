// ui/screens/quiz/QuizLockdownScreenHost.kt
package com.example.campqstudent.ui.screens.quiz

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.campqstudent.viewmodel.QuizViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuizLockdownScreenHost(
    accessCode: String,
    quizViewModel: QuizViewModel,
    onQuizSubmit: (score: Int) -> Unit,
    onQuizError: (String) -> Unit
) {
    // This composable simply forwards to the UI composable that uses the same viewModel instance
    QuizLockdownScreen(accessCode = accessCode, quizViewModel = quizViewModel, onQuizSubmit = onQuizSubmit, onQuizError = onQuizError)
}
