package com.mathdetective.ui.report

import androidx.lifecycle.ViewModel
import com.mathdetective.data.repository.GameRepository
import kotlinx.coroutines.flow.StateFlow
import com.mathdetective.data.model.UserProgress

class ReportViewModel : ViewModel() {
    val userProgress: StateFlow<UserProgress> = GameRepository.userProgress
}