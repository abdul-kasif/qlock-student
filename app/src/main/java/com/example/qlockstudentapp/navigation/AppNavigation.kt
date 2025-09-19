// navigation/AppNavigation.kt
package com.example.qlockstudentapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.qlockstudentapp.ui.screens.auth.EmailOtpScreen
import com.example.qlockstudentapp.ui.screens.profile.ProfileSetupScreen
import com.example.qlockstudentapp.ui.screens.dashboard.DashboardScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "email_otp") {
        composable("email_otp") { EmailOtpScreen(navController) }
        composable("profile_setup") { ProfileSetupScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
    }
}