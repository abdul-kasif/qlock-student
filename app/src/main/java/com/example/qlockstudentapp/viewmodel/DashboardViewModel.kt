package com.example.qlockstudentapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlockstudentapp.api.ApiClient
import com.example.qlockstudentapp.model.response.StudentDashboardResponse
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    var dashboardData by mutableStateOf<StudentDashboardResponse?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    fun loadDashboard() {
        isLoading = true
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val response = ApiClient.getApiService(context).getStudentDashboard()

                if (response.isSuccessful && response.body() != null) {
                    dashboardData = response.body()
                    Log.d("DashboardViewModel", "Dashboard loaded successfully")
                } else {
                    errorMessage = "Failed to load dashboard. Please try again."
                    Log.e("DashboardViewModel", "Load failed: ${response.errorBody()?.string()}")
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