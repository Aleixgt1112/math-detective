package com.mathdetective.data.model

data class Operation(
    val operand1: Int,
    val operand2: Int,
    val result: Int,
    val correctOperator: Char,
    val options: List<Char> = listOf('+', '-', '*', '/')
)