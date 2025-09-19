package com.example.qlockstudentapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlockstudentapp.api.ApiClient
import com.example.qlockstudentapp.model.response.ApiResponse
import com.example.qlockstudentapp.model.request.SendOtpRequest
import com.example.qlockstudentapp.model.request.VerifyOtpRequest
import com.example.qlockstudentapp.model.response.VerifyOtpResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel : ViewModel() {

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

    fun onEmailChanged(newEmail: String) {
        email = newEmail
        clearError()
    }

    fun onOtpChanged(newOtp: String) {
        otp = newOtp
        clearError()
    }

    private fun clearError() {
        errorMessage = ""
    }

    fun sendOtp() {
        if (email.isBlank()) {
            errorMessage = "Please enter your email"
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val request = SendOtpRequest(email = email)
                val response: Response<ApiResponse> =
                    ApiClient.apiService.sentOtp(request)

                if (response.isSuccessful) {
                    showOtpField = true
                    Log.d("AuthViewModel", "OTP sent successfully")
                } else {
                    errorMessage = "Failed to send OTP. Please try again."
                    Log.e("AuthViewModel", "Send OTP failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Network error. Check your connection."
                Log.e("AuthViewModel","Exception: ${e.message}",e)
            } finally {
                isLoading = false
            }
        }
    }

    fun verifyOtp(onSuccess: (VerifyOtpResponse) -> Unit) {
        if (otp.length != 6) {
            errorMessage = "Please enter a 6-digit OTP"
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val request = VerifyOtpRequest(
                    email = email,
                    code = otp,
                    role = "student" // Hardcoded for Student App
                )
                val response: Response<VerifyOtpResponse> =
                    ApiClient.apiService.verifyOtp(request)

                if (response.isSuccessful && response.body() != null) {
                    val verifyResponse = response.body()!!
                    onSuccess(verifyResponse)
                    Log.d("AuthViewModel","OTP verified. New user: ${verifyResponse.is_new_user}")
                } else {
                    errorMessage = "Invalid or expired OTP, Please try again."
                    Log.e("AuthViewModel","Verify OTP failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Network error. Check your connection."
                Log.e("AuthViewModel","Exception: ${e.message}",e)
            } finally {
                isLoading = false
            }
        }
    }
}
