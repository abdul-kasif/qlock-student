// model/response/TestSessionStartResponse.kt
package com.example.qlockstudentapp.model.response

data class TestSessionStartResponse(
    val valid: Boolean,
    val session: TestSession?, // ✅ Now TestSession is nested → resolved
    val can_resume: Boolean? = null,
    val message: String? = null
) {
    // ✅ Define TestSession INSIDE TestSessionStartResponse
    data class TestSession(
        val id: Long,
        val title: String,
        val google_form_url: String,
        val test_duration_minutes: Int,
        val started_at: String // ISO 8601
    )
}