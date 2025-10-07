// viewmodel/QuizValidationViewModel.kt
package com.example.campqstudent.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.campqstudent.api.ApiClient
import kotlinx.coroutines.launch

class QuizValidationViewModel(application: Application) : AndroidViewModel(application) {

    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    private fun clearError() {
        errorMessage = ""
    }

    fun validateAccessCode(accessCode: String, onSuccess: (String, Int) -> Unit, onError: (String) -> Unit) {
        if (accessCode.isBlank()) {
            onError("Access code cannot be empty")
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val response = ApiClient.getApiService(context).checkCodeValidity(accessCode)

                if (response.isSuccessful && response.body() != null) {
                    val quiz = response.body()!!.quiz
                    Log.d("QuizValidationViewModel", "Access code valid")
                    onSuccess(quiz.title, quiz.time_limit_minutes)
                } else {
                    val error = response.errorBody()?.string()?.let { parseErrorMessage(it) }
                        ?: "Invalid or expired access code"
                    onError(error)
                }
            } catch (e: Exception) {
                onError("Network error. Check your connection.")
                Log.e("QuizValidationViewModel", "Exception: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }

    private fun parseErrorMessage(errorBody: String): String {
        return try {
            val json = org.json.JSONObject(errorBody)
            json.optString("error", "Invalid or expired access code")
        } catch (e: Exception) {
            "Invalid or expired access code"
        }
    }
}