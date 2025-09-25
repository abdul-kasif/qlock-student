// ui/screens/quiz/QuizLockdownScreenComposable.kt
package com.example.qlockstudentapp.ui.screens.quiz

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.qlockstudentapp.viewmodel.QuizViewModel
import com.example.qlockstudentapp.model.response.Question
import com.example.qlockstudentapp.ui.components.lockdown.LockdownHeader

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizLockdownScreenComposable(
    accessCode: String,
    quizViewModel: QuizViewModel,
    onQuizSubmit: (score: Int) -> Unit,
    onQuizError: (String) -> Unit
) {
    val context = LocalContext.current
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Fetch quiz + start timer using the passed viewModel
    LaunchedEffect(Unit) {
        quizViewModel.fetchQuiz(accessCode) {
            quizViewModel.startTimer {
                // auto-submit with callbacks into the Activity
                quizViewModel.submitQuiz(
                    onSuccess = { response ->
                        onQuizSubmit(response.score)
                    },
                    onError = { error ->
                        onQuizError(error)
                    }
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            quizViewModel.stopTimer()
        }
    }

    val quiz = quizViewModel.quiz
    val timeLeft = quizViewModel.timeLeft

    if (quiz == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Fixed Header
        LockdownHeader(
            testTitle = quiz.title,
            timeLeft = timeLeft,
            onExitRequest = {
                Toast.makeText(context, "You cannot exit until quiz is submitted.", Toast.LENGTH_LONG).show()
            }
        )

        // Questions List (single visible question)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            itemsIndexed(quiz.questions) { index, question ->
                if (index == currentQuestionIndex) {
                    QuestionCard(
                        question = question,
                        selectedOptionId = quizViewModel.selectedAnswers[question.id],
                        onOptionSelected = { optionId ->
                            quizViewModel.selectAnswer(question.id, optionId)
                        }
                    )
                }
            }
        }

        // Navigation & Submit Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentQuestionIndex > 0) {
                OutlinedButton(
                    onClick = { currentQuestionIndex-- },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Previous")
                }
            }

            if (currentQuestionIndex < quiz.questions.size - 1) {
                Button(
                    onClick = { currentQuestionIndex++ },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Next")
                }
            } else {
                Button(
                    onClick = {
                        if (isSubmitting) return@Button
                        isSubmitting = true
                        quizViewModel.submitQuiz(
                            onSuccess = { response ->
                                isSubmitting = false
                                onQuizSubmit(response.score)
                            },
                            onError = { errorMessage ->
                                isSubmitting = false
                                onQuizError(errorMessage)
                            }
                        )
                    },
                    enabled = !isSubmitting,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSubmitting) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Submit Quiz")
                    }
                }
            }
        }
    }
}
