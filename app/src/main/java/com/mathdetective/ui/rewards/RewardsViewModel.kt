package com.mathdetective.ui.rewards

import androidx.lifecycle.ViewModel
import com.mathdetective.data.model.Avatar
import com.mathdetective.data.model.UserProgress
import com.mathdetective.data.repository.GameRepository
import kotlinx.coroutines.flow.*

class RewardsViewModel : ViewModel() {
    val avatars: StateFlow<List<Avatar>> = GameRepository.avatars
    val userProgress: StateFlow<UserProgress> = GameRepository.userProgress

    // StateFlow para saber cuál avatar está seleccionado
    val selectedAvatarId = userProgress.map { it.selectedAvatarId }.stateIn(
        scope = kotlinx.coroutines.GlobalScope,
        started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
        initialValue = 1
    )

    fun onUnlockAvatar(avatarId: Int) {
        GameRepository.unlockAvatar(avatarId)
    }

    fun onSelectAvatar(avatarId: Int) {
        GameRepository.selectAvatar(avatarId)
    }
}