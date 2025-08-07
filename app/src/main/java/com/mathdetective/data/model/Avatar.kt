package com.mathdetective.data.model

data class Avatar(
    val id: Int,
    val name: String,
    val icon: String,
    val cost: Int,
    val isUnlocked: Boolean
)