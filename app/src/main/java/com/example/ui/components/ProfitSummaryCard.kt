package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FarmProfitCalculationResult
import com.example.model.IndianCurrencyFormatter

@Composable
fun ProfitSummaryCard(
    result: FarmProfitCalculationResult,
    onViewMaxProfitPlan: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isProfitable = result.estimatedProfitMin >= 0
    val roiDisplay = if (result.roiPercentageMin >= 0) {
        "+${result.roiPercentageMin.toInt()}% to +${result.roiPercentageMax.toInt()}%"
    } else {
        "${result.roiPercentageMin.toInt()}% to ${result.roiPercentageMax.toInt()}%"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("profit_summary_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isProfitable) Color(0xFF1B5E20) else Color(0xFFB71C1C)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Badge & Crop Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${result.cropEmoji} ${result.cropName} • ${result.landSizeAcre} Acre(s)",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFFFD54F)
                ) {
                    Text(
                        text = "ROI: $roiDisplay",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF3E2723)
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Highlight: Estimated Net Profit Range
            Text(
                text = "ESTIMATED NET PROFIT",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${IndianCurrencyFormatter.formatCompactRupees(result.estimatedProfitMin)} – ${IndianCurrencyFormatter.formatCompactRupees(result.estimatedProfitMax)}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp
                ),
                color = Color.White
            )

            Text(
                text = "Full Range: ${IndianCurrencyFormatter.formatRupees(result.estimatedProfitMin)} to ${IndianCurrencyFormatter.formatRupees(result.estimatedProfitMax)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = Color.White.copy(alpha = 0.2f), thickness = 1.dp)

            Spacer(modifier = Modifier.height(14.dp))

            // 3-Column Metrics: Investment | Revenue | Break-even
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Total Investment
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Total Investment",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = IndianCurrencyFormatter.formatCompactRupees(result.totalInvestment),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "${IndianCurrencyFormatter.formatRupees(result.costPerAcre)}/acre",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                // Expected Revenue
                Column(modifier = Modifier.weight(1.2f)) {
                    Text(
                        text = "Expected Revenue",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${IndianCurrencyFormatter.formatCompactRupees(result.expectedRevenueMin)}–${IndianCurrencyFormatter.formatCompactRupees(result.expectedRevenueMax)}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFFFE082)
                    )
                    Text(
                        text = "@ ₹${result.expectedPricePerKgMin.toInt()}–₹${result.expectedPricePerKgMax.toInt()}/kg",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                // Break-Even Price
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Break-even Price",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = String.format("₹%.1f/kg", result.breakEvenPricePerKg),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "Threshold",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Expected production yield info
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Agriculture,
                        contentDescription = "Yield",
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Expected Production: ${IndianCurrencyFormatter.formatKg(result.expectedYieldKgMin)} to ${IndianCurrencyFormatter.formatKg(result.expectedYieldKgMax)}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = Color.White
                    )
                }
            }
        }
    }
}
