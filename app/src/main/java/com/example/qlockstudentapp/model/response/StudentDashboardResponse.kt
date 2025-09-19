// model/response/StudentDashboardResponse.kt
package com.example.qlockstudentapp.model.response

data class StudentDashboardResponse(
    val user: UserResponse,
    val taken_tests: List<TakenTestResponse>
)

data class TakenTestResponse(
    val title: String,
    val started_at: String, // ISO 8601 string (we'll parse it)
    val submitted_at: String?, // Can be null
    val status: String // e.g., "submitted", "abandoned"
)