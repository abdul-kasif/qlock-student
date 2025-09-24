// ui/screens/quiz/QuizLockdownScreenHost.kt
package com.example.qlockstudentapp.ui.screens.quiz

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.qlockstudentapp.viewmodel.QuizViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuizLockdownScreenHost(
    accessCode: String,
    quizViewModel: QuizViewModel,
    onQuizSubmit: (score: Int) -> Unit,
    onQuizError: (String) -> Unit
) {
    // This composable simply forwards to the UI composable that uses the same viewModel instance
    QuizLockdownScreenComposable(accessCode = accessCode, quizViewModel = quizViewModel, onQuizSubmit = onQuizSubmit, onQuizError = onQuizError)
}
