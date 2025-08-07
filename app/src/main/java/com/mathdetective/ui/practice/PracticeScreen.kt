package com.mathdetective.ui.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

fun colorForOperator(op: String): Color = when (op) {
    "+" -> Color(0xFF2ECC40) // verde
    "-" -> Color(0xFFFF4136) // rojo
    "×", "*" -> Color(0xFFFF851B) // naranja
    "÷", "/" -> Color(0xFF0074D9) // azul
    else -> Color.Gray
}

@Composable
fun PracticeScreen(viewModel: PracticeViewModel = viewModel()) {
    val operation by viewModel.operation.collectAsState()
    val selectedAvatar by viewModel.selectedAvatar.collectAsState()
    val progress by viewModel.userProgress.collectAsState()

    var feedbackMessage by remember { mutableStateOf("") }
    var feedbackColor by remember { mutableStateOf(Color.Transparent) }
    var showFeedback by remember { mutableStateOf(false) }

    // Auto-hide feedback después de 2 segundos
    LaunchedEffect(showFeedback) {
        if (showFeedback) {
            delay(2000)
            showFeedback = false
            feedbackMessage = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (operation != null) {
            // Mostrar puntos actuales
            Text(
                "Puntos: ${progress.points} ⭐",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))

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
                    val color = colorForOperator(operator.toString())
                    ArcadeButton(
                        text = operator.toString(),
                        color = color,
                        onClick = {
                            val isCorrect = viewModel.checkAnswer(operator)
                            if (isCorrect) {
                                feedbackMessage = "¡Bien hecho!"
                                feedbackColor = Color(0xFF4CAF50)
                                showFeedback = true
                                viewModel.loadNewOperation()
                            } else {
                                feedbackMessage = "¡Inténtalo de nuevo!"
                                feedbackColor = Color(0xFFF44336)
                                showFeedback = true
                            }
                        },
                        modifier = Modifier.size(width = 72.dp, height = 60.dp)
                    )
                }
            }



            Spacer(modifier = Modifier.height(32.dp))

            // Mostrar feedback con globo de diálogo
            if (showFeedback && feedbackMessage.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Avatar
                    Text(
                        text = selectedAvatar?.icon ?: "🕵️",
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    // Globo de diálogo
                    Box {
                        // Globo principal
                        Box(
                            modifier = Modifier
                                .background(
                                    color = feedbackColor,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = feedbackMessage,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Cola del globo (triángulo)
                        Box(
                            modifier = Modifier
                                .offset(x = (-8).dp, y = 8.dp)
                                .size(16.dp)
                                .background(
                                    color = feedbackColor,
                                    shape = androidx.compose.foundation.shape.GenericShape { size, _ ->
                                        // Triángulo apuntando hacia el avatar
                                        moveTo(0f, 0f)
                                        lineTo(size.width, size.height / 2)
                                        lineTo(0f, size.height)
                                        close()
                                    }
                                )
                        )
                    }
                }
            }

        } else {
            CircularProgressIndicator()
        }
    }
}


@Composable
fun ArcadeButton(
    text: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(50.dp), // Esquinas más redondeadas
        colors = ButtonDefaults.buttonColors(containerColor = color),
        elevation = ButtonDefaults.buttonElevation(10.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = Color.White
        )
    }
}

