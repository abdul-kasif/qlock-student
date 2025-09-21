// viewmodel/TestSessionViewModel.kt
package com.example.qlockstudentapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlockstudentapp.api.ApiClient
import com.example.qlockstudentapp.model.request.TestSessionStartRequest
import com.example.qlockstudentapp.model.response.TestSessionStartResponse
import kotlinx.coroutines.launch

class TestSessionViewModel(application: Application) : AndroidViewModel(application) {

    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set
    var testSession by mutableStateOf<TestSessionStartResponse.TestSession?>(null) // ✅ Now valid
        private set
    var canResume by mutableStateOf(false)
        private set

    private fun clearError() {
        errorMessage = ""
    }

    fun startTestSession(accessCode: String, onSuccess: (TestSessionStartResponse.TestSession) -> Unit) {
        if (accessCode.isBlank()) {
            errorMessage = "Access code cannot be empty"
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val request = TestSessionStartRequest(access_code = accessCode)
                val response = ApiClient.getApiService(context).startTestSession(request)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.valid && body.session != null) {
                        testSession = body.session // ✅ Assignment now works
                        canResume = body.can_resume ?: false
                        onSuccess(body.session)
                        Log.d("TestSessionViewModel", "Test session started: ${body.session.title}")
                    } else {
                        errorMessage = body.message ?: "Invalid or expired access code"
                        Log.e("TestSessionViewModel", "Invalid session: ${body.message}")
                    }
                } else {
                    errorMessage = "Failed to start test. Please try again."
                    Log.e("TestSessionViewModel", "API Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Network error. Check your connection."
                Log.e("TestSessionViewModel", "Exception: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }
}