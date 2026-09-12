package com.example.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.CropBenchmark
import com.example.model.CropDatabase
import com.example.model.IndianCurrencyFormatter
import com.example.ui.components.ExpenseBreakdownVisualizer
import com.example.ui.components.ProfitSummaryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmProfitCalculatorScreen(
    viewModel: FarmProfitCalculatorViewModel = viewModel(),
    onNavigateToMaxProfitMode: (cropName: String, quantityTonnes: Double) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var showQuickLandPresets by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "KISANX",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.2.sp
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = "AI INTELLIGENCE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Farm Profit Predictor",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleDisclaimer(true) },
                        modifier = Modifier.testTag("disclaimer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Estimates Disclaimer",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = { viewModel.resetToDefaults() },
                        modifier = Modifier.testTag("reset_defaults_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Benchmarks",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Tagline Banner
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Calculate Before You Cultivate",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Accurate ROI, yield prediction & break-even market price",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // SECTION 1: CROP SELECTOR
            Text(
                text = "1. Select Target Crop",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.testTag("crop_selector_row")
            ) {
                items(CropDatabase.crops) { crop ->
                    val isSelected = crop.id == state.selectedCrop.id
                    CropSelectionChip(
                        crop = crop,
                        isSelected = isSelected,
                        onClick = { viewModel.selectCrop(crop) }
                    )
                }
            }

            // SECTION 2: LAND SIZE INPUT & PRESETS
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "2. Land Area",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${state.selectedCrop.durationDays} • ${state.selectedCrop.season}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.landSizeInput,
                        onValueChange = { viewModel.updateLandSize(it) },
                        label = { Text("Land Size (in Acres)") },
                        placeholder = { Text("e.g. 2.5 or 5") },
                        leadingIcon = {
                            Icon(Icons.Default.Landscape, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        trailingIcon = {
                            Text(
                                text = "Acres",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("land_size_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick Land Preset Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("0.5", "1.0", "2.0", "3.0", "5.0").forEach { preset ->
                            val isCurrent = state.landSizeInput == preset
                            OutlinedButton(
                                onClick = {
                                    viewModel.updateLandSize(preset)
                                    focusManager.clearFocus()
                                },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isCurrent) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "$preset Ac",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 3: EXPENSE BREAKDOWN INPUTS
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "3. Investment Expenses",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Auto-filled with regional benchmarks. Tap to adjust.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    ExpenseInputField(
                        label = "Seed / Sapling Cost",
                        value = state.seedCostInput,
                        onValueChange = { viewModel.updateSeedCost(it) },
                        icon = Icons.Default.Spa,
                        testTag = "seed_cost_input"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExpenseInputField(
                        label = "Fertilizer & Nutrition",
                        value = state.fertilizerCostInput,
                        onValueChange = { viewModel.updateFertilizerCost(it) },
                        icon = Icons.Default.Science,
                        testTag = "fertilizer_cost_input"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExpenseInputField(
                        label = "Labour Cost (Tilling to Harvest)",
                        value = state.labourCostInput,
                        onValueChange = { viewModel.updateLabourCost(it) },
                        icon = Icons.Default.Groups,
                        testTag = "labour_cost_input"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExpenseInputField(
                        label = "Irrigation / Electricity / Water",
                        value = state.irrigationCostInput,
                        onValueChange = { viewModel.updateIrrigationCost(it) },
                        icon = Icons.Default.WaterDrop,
                        testTag = "irrigation_cost_input"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExpenseInputField(
                        label = "Machinery & Tractor Hire",
                        value = state.machineryCostInput,
                        onValueChange = { viewModel.updateMachineryCost(it) },
                        icon = Icons.Default.Toll,
                        testTag = "machinery_cost_input"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExpenseInputField(
                        label = "Pest & Disease Management",
                        value = state.pestCostInput,
                        onValueChange = { viewModel.updatePestCost(it) },
                        icon = Icons.Default.BugReport,
                        testTag = "pest_cost_input"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExpenseInputField(
                        label = "Other Expenses (Transport, packing, etc.)",
                        value = state.otherCostInput,
                        onValueChange = { viewModel.updateOtherCost(it) },
                        icon = Icons.Default.MoreHoriz,
                        testTag = "other_cost_input"
                    )
                }
            }

            // SECTION 4: EXPECTED MARKET SELLING PRICE
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "4. Expected Market Price Range (₹/kg)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Current regional benchmark: ₹${state.selectedCrop.pricePerKgMin.toInt()} – ₹${state.selectedCrop.pricePerKgMax.toInt()}/kg",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = state.customExpectedPriceMinInput,
                            onValueChange = { viewModel.updateExpectedPriceMin(it) },
                            label = { Text("Min Price (₹/kg)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("price_min_input")
                        )

                        OutlinedTextField(
                            value = state.customExpectedPriceMaxInput,
                            onValueChange = { viewModel.updateExpectedPriceMax(it) },
                            label = { Text("Max Price (₹/kg)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("price_max_input")
                        )
                    }
                }
            }

            // SECTION 5: VISUAL SUMMARY & RESULTS
            state.calculationResult?.let { result ->
                Text(
                    text = "5. Visual Summary & Profit Projections",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // High visual impact Profit Summary Card
                ProfitSummaryCard(
                    result = result,
                    onViewMaxProfitPlan = {
                        val tonnes = (result.expectedYieldKgMax / 1000.0).coerceAtLeast(1.0)
                        onNavigateToMaxProfitMode(result.cropName, tonnes)
                    }
                )

                // Multi-color Cost Distribution Visualizer
                ExpenseBreakdownVisualizer(
                    seedCost = result.seedCost,
                    fertilizerCost = result.fertilizerCost,
                    labourCost = result.labourCost,
                    irrigationCost = result.irrigationCost,
                    machineryCost = result.machineryCost,
                    pestCost = result.pestCost,
                    otherCost = result.otherCost,
                    totalInvestment = result.totalInvestment
                )

                // Detailed Unit Economics Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Unit Economics & Risk Analysis",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        UnitMetricRow(
                            label = "Cost of Production per Kg",
                            value = String.format("₹%.2f/kg", result.costPerKgExpected),
                            subtext = "Total investment ÷ expected avg yield",
                            icon = Icons.Default.ReceiptLong
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

                        UnitMetricRow(
                            label = "Break-even Price",
                            value = String.format("₹%.2f/kg", result.breakEvenPricePerKg),
                            subtext = "Selling above this guarantees profit",
                            icon = Icons.Default.TrendingUp,
                            highlightColor = MaterialTheme.colorScheme.primary
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

                        UnitMetricRow(
                            label = "Estimated Profit per Acre",
                            value = "${IndianCurrencyFormatter.formatCompactRupees(result.profitPerAcreMin)} – ${IndianCurrencyFormatter.formatCompactRupees(result.profitPerAcreMax)}",
                            subtext = "Net return normalized per acre",
                            icon = Icons.Default.MonetizationOn
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

                        UnitMetricRow(
                            label = "Crop Risk Profile",
                            value = "${state.selectedCrop.riskLevel} Risk",
                            subtext = "Based on regional disease, weather & price swings",
                            icon = Icons.Default.Shield,
                            highlightColor = when (state.selectedCrop.riskLevel) {
                                "Low" -> Color(0xFF2E7D32)
                                "Medium" -> Color(0xFFF57F17)
                                else -> Color(0xFFC62828)
                            }
                        )
                    }
                }

                // Call To Action: MAX PROFIT MODE BRIDGE
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🚀", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Maximize Your Net Return",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "KISANX Max Profit Mode compares nearby mandis (Vellore, Chennai, Bengaluru, Hosur) including exact transport costs to find where your ${result.cropName} will fetch the absolute highest net realization.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val tonnes = (result.expectedYieldKgMax / 1000.0).coerceAtLeast(1.0)
                                onNavigateToMaxProfitMode(result.cropName, tonnes)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("view_max_profit_plan_button")
                        ) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Compare Markets in Max Profit Mode",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // Legal & Estimations Disclaimer footnote
            Text(
                text = "* Predictions and calculations are estimates based on regional agricultural benchmarks. Actual yields, input costs, and market prices may vary due to weather, micro-climate, and market supply dynamics.",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Disclaimer Dialog
    if (state.showDisclaimerDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleDisclaimer(false) },
            icon = { Icon(Icons.Default.WarningAmber, contentDescription = null, tint = Color(0xFFF57F17)) },
            title = { Text("Model Estimates Disclaimer") },
            text = {
                Text(
                    "The KISANX Farm Profit Predictor provides algorithmic estimates using historical Indian mandi prices and agricultural university cultivation benchmarks.\n\n" +
                    "These calculations do not represent guaranteed financial returns. Farmers are advised to consult local Krishi Vigyan Kendra (KVK) extension officers for crop inputs and monitor live mandi arrivals before harvesting."
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.toggleDisclaimer(false) }) {
                    Text("Understood")
                }
            }
        )
    }
}

@Composable
fun CropSelectionChip(
    crop: CropBenchmark,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = if (isSelected) 4.dp else 1.dp,
        modifier = modifier
            .clickable { onClick() }
            .testTag("crop_chip_${crop.id}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = crop.emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = crop.name,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    ),
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "₹${crop.pricePerKgMin.toInt()}–₹${crop.pricePerKgMax.toInt()}/kg",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = if (isSelected) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ExpenseInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    testTag: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 13.sp) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            Text(
                text = "₹",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 12.dp)
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
    )
}

@Composable
fun UnitMetricRow(
    label: String,
    value: String,
    subtext: String,
    icon: ImageVector,
    highlightColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtext,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = highlightColor
        )
    }
}
