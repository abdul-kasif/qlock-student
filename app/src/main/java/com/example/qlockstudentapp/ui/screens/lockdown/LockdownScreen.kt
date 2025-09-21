package com.example.qlockstudentapp.ui.screens.lockdown

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.qlockstudentapp.utils.CountDownTimer
import kotlinx.coroutines.*

@Composable
fun LockdownScreen(
    navController: NavHostController,
    sessionId: Long,
    title: String,
    googleFormUrl: Any?,
    testDurationMinutes: Int
) {
    val totalSeconds = testDurationMinutes * 60L
    var timeLeft by remember { mutableLongStateOf(totalSeconds) }
    var isTimerRunning by remember { mutableStateOf(true) }

    val timer = remember {
        CountDownTimer(
            totalSeconds = totalSeconds,
            onTick = { seconds ->
                timeLeft = seconds
            },
            onFinish = {
                // Auto-submit when timer ends
                CoroutineScope(Dispatchers.Main).launch {
                    // TODO: Call submit API
                    navController.navigate("dashboard") {
                        popUpTo("lockdown") { inclusive = true }
                    }
                }
            }
        )
    }

    // Start timer when screen is composed
    LaunchedEffect(Unit) {
        timer.start()
    }

    // Cleanup timer when screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            timer.cancel()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Timer Display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (timeLeft < 60) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Test: $title",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Text(
                    text = formatTime(timeLeft),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        // WebView Placeholder (will replace later)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "WebView will load Google Form here",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // Submit Button
        Button(
            onClick = {
                timer.cancel()
                // TODO: Call submit API
                navController.navigate("dashboard") {
                    popUpTo("lockdown") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Submit Test")
        }
    }
}

fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}