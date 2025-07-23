package com.mathdetective.ui.rewards

import androidx.lifecycle.ViewModel
import com.mathdetective.data.model.Avatar
import com.mathdetective.data.model.UserProgress
import com.mathdetective.data.repository.GameRepository
import kotlinx.coroutines.flow.StateFlow

class RewardsViewModel : ViewModel() {
    val avatars: StateFlow<List<Avatar>> = GameRepository.avatars
    val userProgress: StateFlow<UserProgress> = GameRepository.userProgress

    fun onUnlockAvatar(avatarId: Int) {
        GameRepository.unlockAvatar(avatarId)
    }
}