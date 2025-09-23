package com.example.qlockstudentapp.model.response

data class QuizAccessResponse(
    val quiz: Quiz
)

data class Quiz(
    val id: Long,
    val title: String,
    val time_limit_minutes: Int,
    val questions: List<Question>
)

data class Question(
    val id: Long,
    val text: String,
    val options: List<Option>
)

data class Option(
    val id: Long,
    val text: String
)