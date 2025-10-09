// navigation/AppNavigation.kt
package com.example.campqstudent.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.campqstudent.ui.screens.auth.EmailOtpScreen
import com.example.campqstudent.ui.screens.dashboard.DashboardScreen
import com.example.campqstudent.ui.screens.profile.ProfileSetupScreen
import com.example.campqstudent.ui.screens.permission.QuizPermissionScreen
import com.example.campqstudent.ui.screens.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("email_otp") { EmailOtpScreen(navController) }
        composable("profile_setup") { ProfileSetupScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }

        composable(
            route = "quiz_permission/{quizTitle}/{timeLimitMinutes}/{accessCode}",
            arguments = listOf(
                navArgument("quizTitle") { type = NavType.StringType },
                navArgument("timeLimitMinutes") { type = NavType.IntType }, // ✅ Fix: Use IntType
                navArgument("accessCode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val quizTitle = backStackEntry.arguments?.getString("quizTitle") ?: "Quiz"
            val timeLimitMinutes = backStackEntry.arguments?.getInt("timeLimitMinutes") ?: 0 // ✅ getInt()
            val accessCode = backStackEntry.arguments?.getString("accessCode") ?: ""

            QuizPermissionScreen(
                navController = navController,
                quizTitle = quizTitle,
                timeLimitMinutes = timeLimitMinutes,
                accessCode = accessCode
            )
        }
    }
}