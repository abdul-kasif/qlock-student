package com.example.campqstudent.model.response

data class QuizValidateResponse(
    val message: String,
    val quiz: QuizData
)
data class QuizData(
    val id: Long,
    val title: String,
    val time_limit_minutes: Int
)
