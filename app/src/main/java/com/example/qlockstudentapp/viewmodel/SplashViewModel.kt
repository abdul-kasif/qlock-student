// viewmodel/SplashViewModel.kt
package com.example.qlockstudentapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlockstudentapp.api.ApiClient
import com.example.qlockstudentapp.utils.SecureStorage
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    fun checkAuthAndNavigate(onNavigateToDashboard: () -> Unit, onNavigateToLogin: () -> Unit) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = SecureStorage.getInstance(context).getToken()

                if (token != null) {
                    // Token exists → try to validate by calling a protected endpoint (e.g., /dashboard)
                    val response = ApiClient.getApiService(context).getStudentDashboard()

                    if (response.isSuccessful) {
                        Log.d("SplashViewModel", "Token valid → Navigate to Dashboard")
                        onNavigateToDashboard()
                    } else {
                        Log.d("SplashViewModel", "Token invalid/expired → Navigate to Login")
                        SecureStorage.getInstance(context).clearToken() // Clear invalid token
                        onNavigateToLogin()
                    }
                } else {
                    Log.d("SplashViewModel", "No token → Navigate to Login")
                    onNavigateToLogin()
                }
            } catch (e: Exception) {
                Log.e("SplashViewModel", "Network error during auth check", e)
                onNavigateToLogin()
            }
        }
    }
}