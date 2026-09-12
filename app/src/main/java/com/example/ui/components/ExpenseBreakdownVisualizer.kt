package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.IndianCurrencyFormatter

data class ExpenseSlice(
    val label: String,
    val amount: Double,
    val color: Color
)

@Composable
fun ExpenseBreakdownVisualizer(
    seedCost: Double,
    fertilizerCost: Double,
    labourCost: Double,
    irrigationCost: Double,
    machineryCost: Double,
    pestCost: Double,
    otherCost: Double,
    totalInvestment: Double,
    modifier: Modifier = Modifier
) {
    val slices = remember(seedCost, fertilizerCost, labourCost, irrigationCost, machineryCost, pestCost, otherCost) {
        listOf(
            ExpenseSlice("Labour", labourCost, Color(0xFFE53935)),
            ExpenseSlice("Fertilizer", fertilizerCost, Color(0xFF43A047)),
            ExpenseSlice("Machinery", machineryCost, Color(0xFFFB8C00)),
            ExpenseSlice("Seed", seedCost, Color(0xFF1E88E5)),
            ExpenseSlice("Irrigation", irrigationCost, Color(0xFF00ACC1)),
            ExpenseSlice("Pest Control", pestCost, Color(0xFF8E24AA)),
            ExpenseSlice("Other", otherCost, Color(0xFF757575))
        ).filter { it.amount > 0 }
    }

    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(totalInvestment) {
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, animationSpec = tween(700))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("expense_breakdown_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = "Expense Chart",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cost Distribution",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = IndianCurrencyFormatter.formatCompactRupees(totalInvestment),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Multi-segment horizontal progress bar
            if (totalInvestment > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(Color(0xFFE0E0E0))
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        slices.forEach { slice ->
                            val proportion = ((slice.amount / totalInvestment) * animProgress.value).toFloat()
                            if (proportion > 0.001f) {
                                Box(
                                    modifier = Modifier
                                        .weight(proportion.coerceAtLeast(0.001f))
                                        .fillMaxHeight()
                                        .background(slice.color)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Chips / legend of breakdown
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    slices.chunked(2).forEach { rowSlices ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowSlices.forEach { slice ->
                                val pct = if (totalInvestment > 0) (slice.amount / totalInvestment * 100).toInt() else 0
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(slice.color)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Text(
                                                text = "${slice.label} ($pct%)",
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = IndianCurrencyFormatter.formatRupees(slice.amount),
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                            if (rowSlices.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
