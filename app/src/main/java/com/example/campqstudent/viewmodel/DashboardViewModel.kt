// viewmodel/DashboardViewModel.kt
package com.example.campqstudent.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.campqstudent.api.ApiClient
import com.example.campqstudent.model.response.StudentDashboardResponse
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    var dashboardData by mutableStateOf<StudentDashboardResponse?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    fun clearError() {
        errorMessage = ""
    }

    fun loadDashboard() {
        clearError()
        isLoading = true
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val response = ApiClient.getApiService(context).getStudentDashboard()

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        dashboardData = body
                        Log.d("DashboardViewModel", "Dashboard loaded successfully")
                    } else {
                        errorMessage = "Unexpected response from server."
                        Log.e("DashboardViewModel", "Empty body in response")
                    }
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    errorMessage = "Failed to load dashboard: $error"
                    Log.e("DashboardViewModel", "Load failed: $error")
                }
            } catch (e: Exception) {
                errorMessage = "Network error. Check your connection."
                Log.e("DashboardViewModel", "Exception: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }
}
