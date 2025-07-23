package com.mathdetective.ui.practice

import androidx.lifecycle.ViewModel
import com.mathdetective.data.model.Operation
import com.mathdetective.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PracticeViewModel : ViewModel() {

    private val _operation = MutableStateFlow<Operation?>(null)
    val operation = _operation.asStateFlow()

    init {
        loadNewOperation()
    }

    fun loadNewOperation() {
        _operation.value = GameRepository.generateNewOperation()
    }

    // Verifica la respuesta del usuario y actualiza el estado.
    fun checkAnswer(selectedOperator: Char): Boolean {
        val isCorrect = _operation.value?.correctOperator == selectedOperator
        _operation.value?.let {
            GameRepository.submitAnswer(it, isCorrect)
        }
        return isCorrect
    }
}