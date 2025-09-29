// ui/screens/Auth/EmailOtpScreen.kt
package com.example.qlockstudentapp.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.qlockstudentapp.model.request.VerifyOtpRequest
import com.example.qlockstudentapp.model.response.VerifyOtpResponse
import com.example.qlockstudentapp.utils.SecureStorage
import com.example.qlockstudentapp.viewmodel.AuthViewModel

@Composable
fun EmailOtpScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    val email by authViewModel::email
    val otp by authViewModel::otp
    val showOtpField by authViewModel::showOtpField
    val isLoading by authViewModel::isLoading
    val errorMessage by authViewModel::errorMessage

    LaunchedEffect(Unit) {
        authViewModel.onEmailChanged("")
        authViewModel.onOtpChanged("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "QLock Student",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = authViewModel::onEmailChanged,
            label = { Text("Enter your email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = !showOtpField,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { authViewModel.sendOtp() },
            enabled = !isLoading && !showOtpField,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading && !showOtpField) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Send OTP")
            }
        }

        if (showOtpField) {
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = otp,
                onValueChange = authViewModel::onOtpChanged,
                label = { Text("Enter 6-digit OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.verifyOtp { response ->
                        handleOtpVerification(response, navController)
                    }
                },
                enabled = !isLoading && otp.length == 6,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Verify OTP")
                }
            }
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun handleOtpVerification(
    response: VerifyOtpResponse,
    navController: NavHostController
) {
    // Save JWT token securely
    val context = navController.context
    val secureStorage = SecureStorage.getInstance(context)
    secureStorage.saveToken(response.token)

    Log.d("EmailOtpScreen", "JWT Token saved securely")

    // Navigate based on profile status
    if (response.is_new_user) {
        // User is new → profile_complete = false → go to Profile Setup
        navController.navigate("profile_setup") {
            popUpTo("email_otp") { inclusive = true } // Clear back stack
        }
    } else {
        // User exists and profile_complete = true → go to Dashboard
        navController.navigate("dashboard") {
            popUpTo("email_otp") { inclusive = true }
        }
    }
}