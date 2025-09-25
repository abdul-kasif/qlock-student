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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qlockstudentapp.viewmodel.QuizViewModel
import com.example.qlockstudentapp.model.response.Question
import com.example.qlockstudentapp.ui.components.lockdown.LockdownHeader

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizLockdownScreen(
    accessCode: String,
    onQuizSubmit: (score: Int) -> Unit,
    onQuizError: (String) -> Unit
) {
    val context = LocalContext.current
    val quizViewModel: QuizViewModel = viewModel()
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Fetch quiz & start timer
    LaunchedEffect(Unit) {
        quizViewModel.fetchQuiz(accessCode) {
            quizViewModel.startTimer {
                handleAutoSubmit(quizViewModel, context, onQuizSubmit, onQuizError)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { quizViewModel.stopTimer() }
    }

    val quiz = quizViewModel.quiz
    val timeLeft = quizViewModel.timeLeft

    if (quiz == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        LockdownHeader(
            testTitle = quiz.title,
            timeLeft = timeLeft,
            onExitRequest = {
                Toast.makeText(context, "You cannot exit until quiz is submitted.", Toast.LENGTH_LONG).show()
            }
        )

        // Show current question
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

        // Navigation & Submit
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
                ) { Text("Previous") }
            }

            if (currentQuestionIndex < quiz.questions.size - 1) {
                Button(
                    onClick = { currentQuestionIndex++ },
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Next") }
            } else {
                Button(
                    onClick = {
                        if (isSubmitting) return@Button
                        isSubmitting = true
                        handleManualSubmit(quizViewModel, context, onQuizSubmit, onQuizError) {
                            isSubmitting = false
                        }
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
                    } else Text("Submit Quiz")
                }
            }
        }
    }
}

@Composable
fun QuestionCard(
    question: Question,
    selectedOptionId: Long?,
    onOptionSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = question.text,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            question.options.forEach { option ->
                RadioButtonWithText(
                    text = option.text,
                    selected = selectedOptionId == option.id,
                    onSelected = { onOptionSelected(option.id) }
                )
            }
        }
    }
}

@Composable
fun RadioButtonWithText(
    text: String,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onSelected() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelected,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun handleAutoSubmit(
    quizViewModel: QuizViewModel,
    context: Context,
    onQuizSubmit: (Int) -> Unit,
    onQuizError: (String) -> Unit
) {
    quizViewModel.submitQuiz(
        onSuccess = { response -> onQuizSubmit(response.score) },
        onError = { errorMessage ->
            Toast.makeText(context, "Auto-submit failed: $errorMessage", Toast.LENGTH_LONG).show()
            onQuizError(errorMessage)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun handleManualSubmit(
    quizViewModel: QuizViewModel,
    context: Context,
    onQuizSubmit: (Int) -> Unit,
    onQuizError: (String) -> Unit,
    onComplete: () -> Unit
) {
    quizViewModel.submitQuiz(
        onSuccess = { response ->
            onQuizSubmit(response.score)
            onComplete()
        },
        onError = { errorMessage ->
            Toast.makeText(context, "Submit failed: $errorMessage", Toast.LENGTH_LONG).show()
            onQuizError(errorMessage)
            onComplete()
        }
    )
}

fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}
