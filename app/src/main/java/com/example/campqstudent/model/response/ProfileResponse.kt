package com.example.campqstudent.model.response

data class ProfileResponse(
    val message: String,
    val user: UserResponse
)

data class UserResponse(
    val user_id: Long,
    val name: String,
    val email: String,
    val user_personal_id: String,
    val department: String,
    val role: String,
    val profile_complete: Boolean
)
