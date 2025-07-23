package com.mathdetective.ui.rewards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(viewModel: RewardsViewModel = viewModel()) {
    val avatars by viewModel.avatars.collectAsState()
    val progress by viewModel.userProgress.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis Recompensas") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                "Tus Puntos: ${progress.points} ⭐",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(avatars.size) { index ->
                    val avatar = avatars[index]
                    Card(
                        modifier = Modifier.aspectRatio(1f),
                        enabled = !avatar.isUnlocked && progress.points >= avatar.cost,
                        onClick = {
                            if (!avatar.isUnlocked) {
                                viewModel.onUnlockAvatar(avatar.id)
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(avatar.icon, style = MaterialTheme.typography.displayMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(avatar.name, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(8.dp))
                            if (avatar.isUnlocked) {
                                Text("¡Desbloqueado!", color = MaterialTheme.colorScheme.primary)
                            } else {
                                Text("${avatar.cost} Puntos", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}