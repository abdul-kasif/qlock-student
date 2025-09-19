// ui/screens/Dashboard/DashboardScreen.kt
package com.example.qlockstudentapp.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.qlockstudentapp.utils.AuthManager

@Composable
fun DashboardScreen(navController: NavHostController = rememberNavController()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "QLock",
                style = MaterialTheme.typography.headlineLarge
            )

            Button(
                onClick = {
                    // 1. Clear token
                    AuthManager.logout(navController.context)

                    // 2. Navigate to EmailOtpScreen and CLEAR entire back stack
                    navController.navigate("email_otp") {
                        popUpTo(navController.graph.startDestinationId) { // Clear all screens up to start destination
                            inclusive = false // Keep EmailOtpScreen
                        }
                        launchSingleTop = true // Avoid duplicate instances
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Welcome to your Dashboard!",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}