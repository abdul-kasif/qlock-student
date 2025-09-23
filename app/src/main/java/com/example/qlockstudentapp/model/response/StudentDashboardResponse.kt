// model/response/StudentDashboardResponse.kt
package com.example.qlockstudentapp.model.response

data class StudentDashboardResponse(
    val user: UserResponse,
    val taken_quizzes: List<TakenQuizResponse> // ← Updated from taken_tests
)

data class TakenQuizResponse(
    val quiz_title: String,
    val degree: String,
    val semester: String,
    val subject_code: String,
    val subject_name: String,
    val started_at: String,
    val submitted_at: String?,
    val status: String,
    val score: Int
)