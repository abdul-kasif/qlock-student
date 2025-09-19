package com.example.qlockstudentapp.model.request

data class ProfileSetupRequest(
    val user_personal_id: String,
    val name: String,
    val department: String
    )