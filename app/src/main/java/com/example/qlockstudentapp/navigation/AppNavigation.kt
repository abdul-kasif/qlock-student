// navigation/AppNavigation.kt
package com.example.qlockstudentapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.qlockstudentapp.ui.screens.auth.EmailOtpScreen
import com.example.qlockstudentapp.ui.screens.dashboard.DashboardScreen
import com.example.qlockstudentapp.ui.screens.profile.ProfileSetupScreen
import com.example.qlockstudentapp.ui.screens.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("email_otp") { EmailOtpScreen(navController) }
        composable("profile_setup") { ProfileSetupScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) } // Pass navController for logout
    }
}