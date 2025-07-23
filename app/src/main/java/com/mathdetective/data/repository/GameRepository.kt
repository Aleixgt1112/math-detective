package com.mathdetective.data.repository

import com.mathdetective.data.model.Avatar
import com.mathdetective.data.model.Operation
import com.mathdetective.data.model.UserProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

object GameRepository {

    private val _userProgress = MutableStateFlow(UserProgress())
    val userProgress = _userProgress.asStateFlow()

    private val _avatars = MutableStateFlow(
        listOf(
            Avatar(id = 1, name = "Detective Clásico", icon = "🕵️", cost = 0, isUnlocked = true),
            Avatar(id = 2, name = "Robot Matemático", icon = "🤖", cost = 50, isUnlocked = false),
            Avatar(id = 3, name = "Mago de Números", icon = "🧙", cost = 100, isUnlocked = false),
            Avatar(id = 4, name = "Explorador Veloz", icon = "🏃", cost = 150, isUnlocked = false)
        )
    )
    val avatars = _avatars.asStateFlow()

    fun generateNewOperation(): Operation {
        val operator = listOf('+', '-', '*', '÷').random()
        var operand1 = Random.nextInt(1, 11)
        var operand2 = Random.nextInt(1, 11)
        var result: Int = 0

        when (operator) {
            '+' -> result = operand1 + operand2
            '-' -> {
                // Asegurar que el resultado no sea negativo
                if (operand1 < operand2) {
                    val temp = operand1
                    operand1 = operand2
                    operand2 = temp
                }
                result = operand1 - operand2
            }
            '*' -> {
                operand1 = Random.nextInt(1, 11) // Ajustar rango para multiplicación
                operand2 = Random.nextInt(1, 11)
                result = operand1 * operand2
            }
            '/' -> {
                // Asegurar que la división sea exacta
                operand2 = Random.nextInt(1, 6)
                result = Random.nextInt(1, 6)
                operand1 = operand2 * result
            }
        }
        return Operation(operand1, operand2, result, operator)
    }

    // Procesa la respuesta del usuario y actualiza el progreso.
    fun submitAnswer(operation: Operation, isCorrect: Boolean) {
        _userProgress.update { currentProgress ->
            val newPoints = if (isCorrect) currentProgress.points + 10 else currentProgress.points
            val newCorrect = currentProgress.correctAnswers.toMutableMap()
            val newIncorrect = currentProgress.incorrectAnswers.toMutableMap()

            if (isCorrect) {
                newCorrect[operation.correctOperator] = (newCorrect[operation.correctOperator] ?: 0) + 1
            } else {
                newIncorrect[operation.correctOperator] = (newIncorrect[operation.correctOperator] ?: 0) + 1
            }
            currentProgress.copy(
                points = newPoints,
                correctAnswers = newCorrect,
                incorrectAnswers = newIncorrect
            )
        }
    }

    fun unlockAvatar(avatarId: Int) {
        val avatarToUnlock = _avatars.value.find { it.id == avatarId } ?: return
        if (_userProgress.value.points >= avatarToUnlock.cost) {
            _userProgress.update { it.copy(points = it.points - avatarToUnlock.cost) }
            _avatars.update { currentAvatars ->
                currentAvatars.map {
                    if (it.id == avatarId) it.copy(isUnlocked = true) else it
                }
            }
        }
    }
}