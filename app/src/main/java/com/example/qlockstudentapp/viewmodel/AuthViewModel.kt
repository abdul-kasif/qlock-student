// viewmodel/AuthViewModel.kt
package com.example.qlockstudentapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import android.app.Application
import com.example.qlockstudentapp.api.ApiClient
import com.example.qlockstudentapp.model.request.SendOtpRequest
import com.example.qlockstudentapp.model.request.VerifyOtpRequest
import com.example.qlockstudentapp.model.response.ApiResponse
import com.example.qlockstudentapp.model.response.VerifyOtpResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    // UI State
    var email by mutableStateOf("")
        private set
    var otp by mutableStateOf("")
        private set
    var showOtpField by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    // Update Email
    fun onEmailChanged(newEmail: String) {
        email = newEmail
        clearError()
    }

    // Update OTP
    fun onOtpChanged(newOtp: String) {
        otp = newOtp
        clearError()
    }

    // Clear Error
    private fun clearError() {
        errorMessage = ""
    }

    // Send OTP to Backend
    fun sendOtp() {
        if (email.isBlank()) {
            errorMessage = "Please enter your email"
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val request = SendOtpRequest(email = email)
                val response: Response<ApiResponse> =
                    ApiClient.getApiService(context).sentOtp(request) //FIXED

                if (response.isSuccessful) {
                    showOtpField = true
                    Log.d("AuthViewModel", "OTP sent successfully")
                } else {
                    errorMessage = "Failed to send OTP. Please try again."
                    Log.e("AuthViewModel", "Send OTP failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Network error. Check your connection."
                Log.e("AuthViewModel", "Exception: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }

    // Verify OTP with Backend
    fun verifyOtp(onSuccess: (VerifyOtpResponse) -> Unit) {
        if (otp.length != 6) {
            errorMessage = "Please enter a 6-digit OTP"
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val request = VerifyOtpRequest(
                    email = email,
                    code = otp,
                    role = "student"
                )
                val response: Response<VerifyOtpResponse> =
                    ApiClient.getApiService(context).verifyOtp(request) // ✅ FIXED

                if (response.isSuccessful && response.body() != null) {
                    val verifyResponse = response.body()!!
                    onSuccess(verifyResponse)
                    Log.d("AuthViewModel", "OTP verified. New user: ${verifyResponse.is_new_user}")
                } else {
                    errorMessage = "Invalid OTP or expired. Please try again."
                    Log.e("AuthViewModel", "Verify OTP failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Network error. Check your connection."
                Log.e("AuthViewModel", "Exception: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }
}