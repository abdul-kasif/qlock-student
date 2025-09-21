// navigation/AppNavigation.kt
package com.example.qlockstudentapp.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.qlockstudentapp.ui.screens.auth.EmailOtpScreen
import com.example.qlockstudentapp.ui.screens.dashboard.DashboardScreen
import com.example.qlockstudentapp.ui.screens.lockdown.LockdownScreen
import com.example.qlockstudentapp.ui.screens.profile.ProfileSetupScreen
import com.example.qlockstudentapp.ui.screens.splash.SplashScreen
import java.net.URLDecoder

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("email_otp") { EmailOtpScreen(navController) }
        composable("profile_setup") { ProfileSetupScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) } // Pass navController for logout
        composable("lockdown/{sessionId}/{title}/{googleFormUrl}/{testDurationMinutes}") { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId")?.toLong() ?: 0L
            val title = backStackEntry.arguments?.getString("title") ?: "Test"
            val encodedUrl = backStackEntry.arguments?.getString("googleFormUrl") ?: ""
            val googleFormUrl = try {
                URLDecoder.decode(encodedUrl, "UTF-8")
            } catch (e: Exception) {
                Log.e("AppNavigation", "Error decoding URL: ${e.message}")
            }
            val testDurationMinutes = backStackEntry.arguments?.getString("testDurationMinutes")?.toInt() ?: 0
            LockdownScreen(navController, sessionId, title, googleFormUrl, testDurationMinutes)
        }
    }
}