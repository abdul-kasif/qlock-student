// MainActivity.kt
package com.example.qlockstudentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qlockstudentapp.ui.theme.QLockStudentAppTheme
import com.example.qlockstudentapp.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QLockStudentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmailOtpScreen()
                }
            }
        }
    }
}

@Composable
fun EmailOtpScreen(
    authViewModel: AuthViewModel = viewModel()
) {
    val email by authViewModel::email
    val otp by authViewModel::otp
    val showOtpField by authViewModel::showOtpField
    val isLoading by authViewModel::isLoading
    val errorMessage by authViewModel::errorMessage

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
            enabled = !showOtpField, // Disable after OTP sent
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
                visualTransformation = PasswordVisualTransformation(), // Hide OTP digits
                singleLine = true,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.verifyOtp { response ->
                        // TODO: Navigate based on profile_complete
                        // For now, just log and show success
                        println("JWT Token: ${response.token}")
                        println("Is New User: ${response.is_new_user}")
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