package com.mathdetective.data.model

data class UserProgress(
    val points: Int = 0,
    val correctAnswers: Map<Char, Int> = emptyMap(),
    val incorrectAnswers: Map<Char, Int> = emptyMap()
)