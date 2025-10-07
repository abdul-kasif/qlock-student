package com.example.campqstudent.model.request

data class VerifyOtpRequest(
    val email: String,
    val code: String,
    val role: String, //student -> hardcoded this in app
)