// ui/screens/lockdown/LockdownScreen.kt
package com.example.qlockstudentapp.ui.screens.lockdown

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.qlockstudentapp.ui.components.lockdown.LockdownHeader
import com.example.qlockstudentapp.ui.components.lockdown.LockdownWebView
import com.example.qlockstudentapp.utils.CountDownTimer
import com.example.qlockstudentapp.utils.LockdownManager
import com.example.qlockstudentapp.viewmodel.TestSubmissionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockdownScreen(
    navController: NavHostController,
    sessionId: Long,
    title: String,
    googleFormUrl: Any?,
    testDurationMinutes: Int
) {
    val context = LocalContext.current
    val testSubmissionViewModel: TestSubmissionViewModel = viewModel()
    val totalSeconds = testDurationMinutes * 60L
    var timeLeft by remember { mutableLongStateOf(totalSeconds) }
    var isTimerRunning by remember { mutableStateOf(true) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Initialize lockdown features
    LaunchedEffect(Unit) {
        enableLockdownMode(context)
    }

    // Initialize timer
    val timer = remember {
        CountDownTimer(
            totalSeconds = totalSeconds,
            onTick = { seconds ->
                timeLeft = seconds
            },
            onFinish = {
                CoroutineScope(Dispatchers.Main).launch {
                    handleAutoSubmit(
                        testSubmissionViewModel = testSubmissionViewModel,
                        sessionId = sessionId,
                        context = context,
                        navController = navController
                    )
                }
            }
        ).apply { start() }
    }

    // Cleanup on exit
    DisposableEffect(Unit) {
        onDispose {
            timer.cancel()
            disableLockdownMode(context)
        }
    }

    // Main UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Fixed Header
        LockdownHeader(
            testTitle = title,
            timeLeft = timeLeft,
            onExitRequest = {
                // Show warning — cannot exit manually
                Toast.makeText(context, "You cannot exit until test is submitted.", Toast.LENGTH_LONG).show()
            }
        )

        // WebView Container
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            LockdownWebView(
                url = googleFormUrl as String,
                onWebViewLoaded = {
                    // Optional: track when form is loaded
                }
            )
        }

        // Fixed Submit Button (Bottom)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Button(
                onClick = {
                    if (isSubmitting) return@Button
                    isSubmitting = true
                    timer.cancel()
                    handleManualSubmit(
                        testSubmissionViewModel = testSubmissionViewModel,
                        sessionId = sessionId,
                        context = context,
                        navController = navController,
                        onComplete = { isSubmitting = false }
                    )
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSubmitting) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.error
                )
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 3.dp,
                        color = MaterialTheme.colorScheme.onError
                    )
                } else {
                    Text(
                        text = "SUBMIT TEST",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onError
                        )
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun enableLockdownMode(context: Context) {
    val activity = LockdownManager.getCurrentActivity(context)
    activity?.let {
        LockdownManager.enableLockdownMode(it)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun disableLockdownMode(context: Context) {
    val activity = LockdownManager.getCurrentActivity(context)
    activity?.let {
        LockdownManager.disableLockdownMode(it)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun handleAutoSubmit(
    testSubmissionViewModel: TestSubmissionViewModel,
    sessionId: Long,
    context: Context,
    navController: NavHostController
) {
    testSubmissionViewModel.submitTestSession(
        sessionId = sessionId,
        onSuccess = {
            disableLockdownMode(context)
            navController.navigate("dashboard") {
                popUpTo("lockdown") { inclusive = true }
            }
        },
        onError = { errorMessage ->
            // Still navigate to dashboard even if submit fails (timer ended — user should not be trapped)
            disableLockdownMode(context)
            navController.navigate("dashboard") {
                popUpTo("lockdown") { inclusive = true }
            }
            Toast.makeText(context, "Auto-submit failed: $errorMessage", Toast.LENGTH_LONG).show()
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun handleManualSubmit(
    testSubmissionViewModel: TestSubmissionViewModel,
    sessionId: Long,
    context: Context,
    navController: NavHostController,
    onComplete: () -> Unit
) {
    testSubmissionViewModel.submitTestSession(
        sessionId = sessionId,
        onSuccess = {
            disableLockdownMode(context)
            navController.navigate("dashboard") {
                popUpTo("lockdown") { inclusive = true }
            }
            onComplete()
        },
        onError = { errorMessage ->
            disableLockdownMode(context)
            navController.navigate("dashboard") {
                popUpTo("lockdown") { inclusive = true }
            }
            Toast.makeText(context, "Submit failed: $errorMessage", Toast.LENGTH_LONG).show()
            onComplete()
        }
    )
}

fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}