// model/request/QuizSubmissionRequest.kt
package com.example.qlockstudentapp.model.request

data class QuizSubmissionRequest(
    val quiz_id: Long,
    val answers: List<Answer>
)

data class Answer(
    val question_id: Long,
    val selected_option_id: Long
)