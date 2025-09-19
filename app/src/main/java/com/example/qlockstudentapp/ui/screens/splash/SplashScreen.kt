// ui/screens/splash/SplashScreen.kt
package com.example.qlockstudentapp.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.qlockstudentapp.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    navController: NavHostController,
    splashViewModel: SplashViewModel = viewModel()
) {
    // Check auth state when screen is composed
    LaunchedEffect(Unit) {
        splashViewModel.checkAuthAndNavigate(
            onNavigateToDashboard = {
                navController.navigate("dashboard") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            },
            onNavigateToLogin = {
                navController.navigate("email_otp") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        )
    }

    // Show loading indicator while checking
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.primary
        )
    }
}