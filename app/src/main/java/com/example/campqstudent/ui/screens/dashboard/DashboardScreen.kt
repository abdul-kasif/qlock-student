package com.example.campqstudent.ui.screens.dashboard

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.campqstudent.ui.components.dashboard.DashboardContent
import com.example.campqstudent.utils.AuthManager
import com.example.campqstudent.viewmodel.QuizValidationViewModel // ✅ Add import

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController) {
    val context = LocalContext.current
    val quizValidationViewModel: QuizValidationViewModel = viewModel() // ✅ Initialize



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CampQ",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            AuthManager.logout(navController.context)
                            navController.navigate("email_otp") {
                                popUpTo(navController.graph.id) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        content = { paddingValues ->
            DashboardContent(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                onJoinTest = { accessCode ->
                    // ✅ Validate BEFORE launching activity
                    quizValidationViewModel.validateAccessCode(
                        accessCode = accessCode,
                        onSuccess = { quizTitle, timeLimitMinutes ->
                            // Navigate to permission screen with data
                            navController.navigate("quiz_permission/$quizTitle/$timeLimitMinutes/$accessCode")
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                onInvalidAccessCode = {
                    Toast.makeText(context, "Please enter a 6-digit access code", Toast.LENGTH_SHORT).show()
                }
            )
        }
    )
}