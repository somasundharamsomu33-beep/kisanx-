package com.example.ui.maxprofit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.IndianCurrencyFormatter
import com.example.model.MandiComparison
import com.example.ui.components.MaxProfitComparisonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaxProfitModeScreen(
    cropName: String = "Tomato",
    quantityTonnes: Double = 5.0,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val comparisons = remember(cropName, quantityTonnes) {
        val qtyKg = quantityTonnes * 1000.0
        listOf(
            MandiComparison(
                mandiName = "Bengaluru",
                state = "Karnataka",
                distanceKm = 210,
                marketPricePerKg = 27.0,
                transportCost = 13000.0,
                otherHandlingCost = 3000.0,
                grossRevenue = qtyKg * 27.0,
                netFarmerRealization = (qtyKg * 27.0) - 13000.0 - 3000.0,
                isRecommendedBest = true,
                advantageOverLocalRupees = 25000.0,
                arrivalTonnesPerDay = 48.0,
                supplyPressure = "Low"
            ),
            MandiComparison(
                mandiName = "Chennai (Koyambedu)",
                state = "Tamil Nadu",
                distanceKm = 140,
                marketPricePerKg = 24.0,
                transportCost = 8000.0,
                otherHandlingCost = 2500.0,
                grossRevenue = qtyKg * 24.0,
                netFarmerRealization = (qtyKg * 24.0) - 8000.0 - 2500.0,
                isRecommendedBest = false,
                advantageOverLocalRupees = 15500.0,
                arrivalTonnesPerDay = 75.0,
                supplyPressure = "Medium"
            ),
            MandiComparison(
                mandiName = "Hosur",
                state = "Tamil Nadu",
                distanceKm = 175,
                marketPricePerKg = 25.0,
                transportCost = 10000.0,
                otherHandlingCost = 2500.0,
                grossRevenue = qtyKg * 25.0,
                netFarmerRealization = (qtyKg * 25.0) - 10000.0 - 2500.0,
                isRecommendedBest = false,
                advantageOverLocalRupees = 18500.0,
                arrivalTonnesPerDay = 35.0,
                supplyPressure = "Low"
            ),
            MandiComparison(
                mandiName = "Vellore",
                state = "Tamil Nadu",
                distanceKm = 25,
                marketPricePerKg = 20.0,
                transportCost = 4000.0,
                otherHandlingCost = 2000.0,
                grossRevenue = qtyKg * 20.0,
                netFarmerRealization = (qtyKg * 20.0) - 4000.0 - 2000.0,
                isRecommendedBest = false,
                advantageOverLocalRupees = 0.0,
                arrivalTonnesPerDay = 120.0,
                supplyPressure = "High"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "MAX PROFIT MODE",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            ),
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = "$cropName — $quantityTonnes Tonnes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            item {
                // AI Recommendation Highlight Box
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🏆", fontSize = 26.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Best Estimated Net Return: Bengaluru",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Estimated additional net profit compared with Vellore: ₹25,000.\nEven after ₹13,000 transport cost, the higher wholesale price (₹27/kg vs ₹20/kg) gives you ₹1,19,000 net in hand.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Predictions are estimates and actual prices, costs and profits may vary.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Mandi Comparison (Net Realization)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(comparisons) { item ->
                MaxProfitComparisonCard(comparison = item)
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("back_to_calculator_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Calculate, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Back to Farm Profit Calculator")
                }
            }
        }
    }
}
