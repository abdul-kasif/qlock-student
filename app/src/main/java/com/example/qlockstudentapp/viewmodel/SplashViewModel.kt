// viewmodel/SplashViewModel.kt
package com.example.qlockstudentapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlockstudentapp.api.ApiClient
import com.example.qlockstudentapp.model.response.StudentDashboardResponse
import com.example.qlockstudentapp.utils.SecureStorage
import kotlinx.coroutines.launch
import retrofit2.Response

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    fun checkAuthAndNavigate(
        onNavigateToDashboard: () -> Unit,
        onNavigateToLogin: () -> Unit
    ) {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val storage = SecureStorage.getInstance(context)
            val token = storage.getToken()

            if (token == null) {
                Log.d("SplashViewModel", "No token → Navigate to Login")
                onNavigateToLogin()
                return@launch
            }

            try {
                val response: Response<StudentDashboardResponse> =
                    ApiClient.getApiService(context).getStudentDashboard()

                if (!response.isSuccessful) {
                    Log.w("SplashViewModel", "Invalid/expired token → ${response.code()}")
                    storage.clearToken()
                    onNavigateToLogin()
                    return@launch
                }

                val body = response.body()
                Log.d("SplashViewModel", "Dashboard response: $body")

                when {
                    body?.user?.profile_complete == true -> {
                        Log.d("SplashViewModel", "✅ Profile complete → Navigate to Dashboard")
                        onNavigateToDashboard()
                    }
                    body?.user?.profile_complete == false -> {
                        Log.d("SplashViewModel", "❌ Profile not complete → Navigate to Login")
                        storage.clearToken()
                        onNavigateToLogin()
                    }
                    else -> {
                        Log.w("SplashViewModel", "⚠️ Unexpected response → Navigate to Login")
                        storage.clearToken()
                        onNavigateToLogin()
                    }
                }
            } catch (e: Exception) {
                Log.e("SplashViewModel", "⚠️ Network error during auth check", e)
                onNavigateToLogin()
            }
        }
    }
}
