package com.example.model

data class MandiComparison(
    val mandiName: String,
    val state: String,
    val distanceKm: Int,
    val marketPricePerKg: Double,
    val transportCost: Double,
    val otherHandlingCost: Double,
    val grossRevenue: Double,
    val netFarmerRealization: Double,
    val isRecommendedBest: Boolean = false,
    val advantageOverLocalRupees: Double = 0.0,
    val arrivalTonnesPerDay: Double = 0.0,
    val supplyPressure: String = "Medium" // Low, Medium, High
)
