// ui/screens/dashboard/DashboardScreen.kt
package com.example.qlockstudentapp.ui.screens.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.qlockstudentapp.ui.components.dashboard.DashboardContent
import com.example.qlockstudentapp.utils.AuthManager
import com.example.qlockstudentapp.viewmodel.TestSessionViewModel // ✅ Add this import
import java.net.URLEncoder

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController) {
    // ✅ Initialize TestSessionViewModel here
    val testSessionViewModel: TestSessionViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "QLock",
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
                                popUpTo(navController.graph.id) {
                                    inclusive = false
                                }
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
                // ✅ Pass the callback that uses testSessionViewModel + navController
                onJoinTest = { accessCode ->
                    testSessionViewModel.startTestSession(accessCode) { session ->
                        navController.navigate(
                            "lockdown/${session.id}/${session.title}/${URLEncoder.encode(session.google_form_url, "UTF-8")}/${session.test_duration_minutes}"
                        )
                    }
                },
                onInvalidAccessCode = {
                    // TODO: Show error message
                }
            )
        }
    )
}