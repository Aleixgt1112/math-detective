package com.mathdetective.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onPlayClick: () -> Unit,
    onProgressClick: () -> Unit,
    onRewardsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Math Detective 🕵️",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(64.dp))
        Button(
            onClick = onPlayClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Jugar", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onProgressClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mi Progreso", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRewardsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mis Recompensas", style = MaterialTheme.typography.titleLarge)
        }
    }
}