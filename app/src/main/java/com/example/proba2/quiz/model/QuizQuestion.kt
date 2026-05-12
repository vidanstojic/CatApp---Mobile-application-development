package com.example.proba2.quiz.model

data class QuizQuestion(
    val id: Int,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val imageUrl: String? = null
)

