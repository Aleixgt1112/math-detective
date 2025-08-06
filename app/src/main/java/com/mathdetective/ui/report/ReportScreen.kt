package com.mathdetective.ui.report


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(viewModel: ReportViewModel = viewModel()) {
    val progress by viewModel.userProgress.collectAsState()
    val allOperators = listOf('+', '-', '*', '/')

// Calcular la mejor precisión
    val bestAccuracy = allOperators.maxOfOrNull { op ->
        val correct = progress.correctAnswers[op] ?: 0
        val incorrect = progress.incorrectAnswers[op] ?: 0
        val total = correct + incorrect
        if (total > 0) correct.toFloat() / total else 0f
    } ?: 0f

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
            //Total de Estrellitas
            item {
                Text("Puntos Totales: ${progress.points} ⭐", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(10.dp))
            }
            // Gráfico de Habilidad
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text("Gráfico de Habilidad", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                val operatorAccuracies = allOperators.map { op ->
                    val correct = progress.correctAnswers[op] ?: 0
                    val incorrect = progress.incorrectAnswers[op] ?: 0
                    val total = correct + incorrect
                    if (total > 0) correct.toFloat() / total else 0f
                }

                Canvas(
                    modifier = Modifier
                        .size(300.dp)
                        .padding(16.dp)
                ) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.minDimension / 2.5f
                    val step = radius / 4 // Para círculos concéntricos
                    val angles = listOf(0f, 90f, 180f, 270f)
                    val operatorLabels = listOf("+", "-", "*", "/")

                    val operatorAccuracies = allOperators.map { op ->
                        val correct = progress.correctAnswers[op] ?: 0
                        val incorrect = progress.incorrectAnswers[op] ?: 0
                        val total = correct + incorrect
                        if (total > 0) correct.toFloat() / total else 0f
                    }

                    // 🕸️ Dibujar círculos concéntricos (niveles)
                    repeat(4) { i ->
                        drawCircle(
                            color = Color.LightGray.copy(alpha = 0.3f),
                            radius = step * (i + 1),
                            center = center,
                            style = Stroke(width = 1.dp.toPx())
                        )
                    }

                    // 🕸️ Líneas radiales
                    angles.forEach { angleDeg ->
                        val angleRad = Math.toRadians(angleDeg.toDouble()).toFloat()
                        val endX = center.x + radius * cos(angleRad)
                        val endY = center.y + radius * sin(angleRad)
                        drawLine(
                            color = Color.LightGray.copy(alpha = 0.5f),
                            start = center,
                            end = Offset(endX, endY),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // 🕷️ Dibujar área del radar (polígono)
                    val points = operatorAccuracies.mapIndexed { index, accuracy ->
                        val angleRad = Math.toRadians(angles[index].toDouble()).toFloat()
                        val valueRadius = radius * accuracy
                        Offset(
                            x = center.x + valueRadius * cos(angleRad),
                            y = center.y + valueRadius * sin(angleRad)
                        )
                    }

                    if (points.isNotEmpty()) {
                        val path = Path().apply {
                            moveTo(points[0].x, points[0].y)
                            for (i in 1 until points.size) {
                                lineTo(points[i].x, points[i].y)
                            }
                            close()
                        }

                        drawPath(
                            path = path,
                            color = Color(0xFF4CAF50).copy(alpha = 0.4f), // verde con transparencia
                            style = Fill
                        )

                        drawPath(
                            path = path,
                            color = Color(0xFF4CAF50),
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }

                    // 🏷️ Etiquetas de operadores
                    operatorLabels.forEachIndexed { index, label ->
                        val angleRad = Math.toRadians(angles[index].toDouble()).toFloat()
                        val labelOffset =  radius + 24.dp.toPx()
                        val labelX = center.x + labelOffset * cos(angleRad)
                        val labelY = center.y + labelOffset * sin(angleRad)

                        drawContext.canvas.nativeCanvas.drawText(
                            label,
                            labelX,
                            labelY,
                            android.graphics.Paint().apply {
                                textSize = 42f
                                color = android.graphics.Color.BLACK
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }

            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Rendimiento por Operación", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
            }

            //Operadores y sus estadísticas
            items(allOperators.size) { index ->
                val operator = allOperators[index]
                val correct = progress.correctAnswers[operator] ?: 0
                val incorrect = progress.incorrectAnswers[operator] ?: 0
                val total = correct + incorrect
                val accuracy = if (total > 0) correct.toFloat() / total else 0f

                val barColor = when {
                    accuracy == bestAccuracy && accuracy > 0f -> Color(0xFF4CAF50) // Verde
                    accuracy < 0.5f -> Color(0xFFFF5252) // Rojo
                    else -> MaterialTheme.colorScheme.primary // Azul por defecto
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Operación: $operator", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Aciertos: $correct | Fallos: $incorrect")
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = accuracy,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = barColor
                        )
                    }
                }
            }


        }
    }
}
