package com.example.campqstudent.model.response

data class VerifyOtpResponse(
    val message: String,
    val is_new_user: Boolean,
    val user_id: Long,
    val role: String,
    val token: String,
)