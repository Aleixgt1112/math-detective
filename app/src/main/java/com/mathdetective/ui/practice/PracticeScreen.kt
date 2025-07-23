package com.mathdetective.ui.practice

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PracticeScreen(viewModel: PracticeViewModel = viewModel()) {
    val operation by viewModel.operation.collectAsState()
    var feedbackMessage by remember { mutableStateOf("") }
    var feedbackColor by remember { mutableStateOf(Color.Transparent) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (operation != null) {
            Text(
                text = "${operation!!.operand1}  ?  ${operation!!.operand2} = ${operation!!.result}",
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                operation!!.options.forEach { operator ->
                    Button(
                        modifier = Modifier.size(72.dp),
                        onClick = {
                            val isCorrect = viewModel.checkAnswer(operator)
                            if (isCorrect) {
                                feedbackMessage = "¡Bien hecho! ✅"
                                feedbackColor = Color.Green.copy(alpha = 0.7f)
                                // Cargar nueva operación después de un acierto
                                viewModel.loadNewOperation()
                            } else {
                                feedbackMessage = "Inténtalo de nuevo ❌"
                                feedbackColor = Color.Red.copy(alpha = 0.7f)
                            }
                        }
                    ) {
                        Text(text = operator.toString(), style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = feedbackMessage,
                style = MaterialTheme.typography.titleLarge,
                color = feedbackColor
            )

        } else {
            CircularProgressIndicator()
        }
    }
}