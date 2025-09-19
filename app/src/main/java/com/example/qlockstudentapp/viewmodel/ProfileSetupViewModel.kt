package com.example.qlockstudentapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlockstudentapp.api.ApiClient
import com.example.qlockstudentapp.model.request.ProfileSetupRequest
import com.example.qlockstudentapp.utils.SecureStorage
import kotlinx.coroutines.launch

class ProfileSetupViewModel(application: Application): AndroidViewModel(application) {

    // UI states
    var userPersonalId by mutableStateOf("")
        private set
    var name by mutableStateOf("")
        private set
    var department by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set
    var isDepartmentDropdownExpanded by mutableStateOf(false)
        private set


    fun onUserPersonalIdChanged(text: String) {
        userPersonalId = text
        clearError()
    }

    fun onNameChanged(text: String) {
        name = text
        clearError()
    }

    fun onDepartmentChanged(text: String) {
        department = text
        clearError()
    }

    fun onDepartmentDropdownExpandedChanged(expanded: Boolean) {
        isDepartmentDropdownExpanded = expanded
    }
    private fun clearError() {
        errorMessage = ""
    }

    fun submitProfile(onSuccess: () -> Unit) {
        if (userPersonalId.isBlank() || name.isBlank() || department.isBlank()) {
            errorMessage = "All fields are required"
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = SecureStorage.getInstance(context).getToken()

                if (token == null) {
                    errorMessage = "Authentication required. Please log in again."
                    return@launch
                }

                // Set token in header
                ApiClient.getApiService(context).setupProfile(
                    ProfileSetupRequest(
                        user_personal_id = userPersonalId,
                        name = name,
                        department = department
                    )
                ).also { response ->
                    if (response.isSuccessful && response.body() != null) {
                        Log.d("ProfileSetupViewModel", "Profile setup successful")
                        onSuccess()
                    } else {
                        errorMessage = "Failed to save profile. Please try again."
                        Log.e("ProfileSetupViewModel","Profile setup failed: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Network error. Check your connection."
                Log.e("ProfileSetupViewModel","Exception: ${e.message}",e)
            } finally {
                isLoading = false
            }
        }
    }
}