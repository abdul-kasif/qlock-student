package com.example.qlockstudentapp.model.response

data class QuizSubmissionResponse(
    val id: Long,
    val started_at: String,
    val submitted_at: String,
    val status: String,
    val score: Int,
    val student: Student,
    val answers: List<SubmittedAnswer>
)

data class Student(
    val id: Long,
    val email: String,
    val user_personal_id: String?,
    val name: String?,
    val department: String?,
    val role: String,
    val profile_complete: Boolean
)

data class SubmittedAnswer(
    val id: Long,
    val is_correct: Boolean
)