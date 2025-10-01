package com.example.qlockstudentapp.ui.screens.quiz

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun QuizPermissionScreen(
    navController: NavController,
    quizTitle: String,
    timeLimitMinutes: Int,
    accessCode: String
) {
    val context = LocalContext.current
    val activity = context as Activity
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Lockdown",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Important Exam Instructions",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        InstructionCard(
            icon = Icons.Default.Info,
            title = "Exam Rules",
            content = """
                • You must complete the quiz in one sitting.
                • Switching apps, using back button, or exiting will auto-submit your answers.
                • Screenshots and screen recording are disabled.
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(16.dp))

        InstructionCard(
            icon = Icons.Default.Timer,
            title = "Time Limit",
            content = "This quiz has a time limit of $timeLimitMinutes minutes. When time ends, your answers will be automatically submitted."
        )

        Spacer(modifier = Modifier.height(16.dp))

        InstructionCard(
            icon = Icons.Default.Lock,
            title = "Lockdown Mode",
            content = "To ensure exam integrity, this app will enter full-screen lockdown mode. You will not be able to leave until the quiz is submitted."
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ✅ Accept → request pinning and go to quiz
        Button(
            onClick = {
                QuizPinningLauncherActivity.start(context, accessCode)
                activity.finish()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "I Understand & Accept",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ❌ Decline → go to dashboard
        OutlinedButton(
            onClick = {
                navController.navigate("dashboard") { popUpTo("quiz_permission") { inclusive = true } }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Cancel & Return to Dashboard",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun InstructionCard(icon: ImageVector, title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.Top) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
