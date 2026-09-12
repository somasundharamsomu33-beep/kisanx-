package com.example.model

/**
 * Encapsulates the calculation inputs and resulting metrics for the Farm Profit Predictor.
 */
data class FarmProfitCalculationResult(
    val landSizeAcre: Double,
    val cropName: String,
    val cropEmoji: String,
    val totalInvestment: Double,
    val seedCost: Double,
    val fertilizerCost: Double,
    val labourCost: Double,
    val irrigationCost: Double,
    val machineryCost: Double,
    val pestCost: Double,
    val otherCost: Double,
    // Yield & Revenue
    val expectedYieldKgMin: Double,
    val expectedYieldKgMax: Double,
    val expectedPricePerKgMin: Double,
    val expectedPricePerKgMax: Double,
    val expectedRevenueMin: Double,
    val expectedRevenueMax: Double,
    // Profit
    val estimatedProfitMin: Double,
    val estimatedProfitMax: Double,
    // ROI & Financial ratios
    val roiPercentageMin: Double,
    val roiPercentageMax: Double,
    val breakEvenPricePerKg: Double,
    // Unit economics
    val costPerKgExpected: Double,
    val costPerAcre: Double,
    val profitPerAcreMin: Double,
    val profitPerAcreMax: Double
)
