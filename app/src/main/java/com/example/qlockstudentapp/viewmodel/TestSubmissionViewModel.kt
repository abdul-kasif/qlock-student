// viewmodel/TestSubmissionViewModel.kt
package com.example.qlockstudentapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlockstudentapp.api.ApiClient
import com.example.qlockstudentapp.model.request.TestSessionSubmitRequest
import kotlinx.coroutines.launch

class TestSubmissionViewModel(application: Application) : AndroidViewModel(application) {

    fun submitTestSession(
        sessionId: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val request = TestSessionSubmitRequest(session_id = sessionId)
                val response = ApiClient.getApiService(context).submitTestSession(request)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("TestSubmissionViewModel", "Test submitted successfully")
                    onSuccess()
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    onError("Failed to submit test: $error")
                }
            } catch (e: Exception) {
                onError("Network error: ${e.message}")
                Log.e("TestSubmissionViewModel", "Exception: ${e.message}", e)
            }
        }
    }
}