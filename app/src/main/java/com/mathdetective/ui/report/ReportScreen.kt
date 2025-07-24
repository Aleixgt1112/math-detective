package com.mathdetective.ui.report


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(viewModel: ReportViewModel = viewModel()) {
    val progress by viewModel.userProgress.collectAsState()
    val allOperators = listOf('+', '-', '*', '/')

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Progreso") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("Puntos Totales: ${progress.points} ⭐", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(24.dp))
                Text("Rendimiento por Operación", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(allOperators.size) { index ->
                val operator = allOperators[index]
                val correct = progress.correctAnswers[operator] ?: 0
                val incorrect = progress.incorrectAnswers[operator] ?: 0
                val total = correct + incorrect
                val accuracy = if (total > 0) (correct.toFloat() / total) else 0f

                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Operación: $operator", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Aciertos: $correct | Fallos: $incorrect")
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = accuracy,
                            modifier = Modifier.fillMaxWidth().height(8.dp)
                        )
                    }
                }
            }
        }
    }
}