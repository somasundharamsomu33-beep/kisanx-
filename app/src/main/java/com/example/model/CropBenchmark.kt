package com.example.model

/**
 * Standard crops supported in KISANX with regional economic benchmarks.
 */
data class CropBenchmark(
    val id: String,
    val name: String,
    val emoji: String,
    val durationDays: String,
    val season: String,
    // Per acre baseline costs in INR
    val defaultSeedCostPerAcre: Double,
    val defaultFertilizerCostPerAcre: Double,
    val defaultLabourCostPerAcre: Double,
    val defaultIrrigationCostPerAcre: Double,
    val defaultMachineryCostPerAcre: Double,
    val defaultPestCostPerAcre: Double,
    val defaultOtherCostPerAcre: Double,
    // Production & Market price benchmarks
    val yieldKgPerAcreMin: Double,
    val yieldKgPerAcreMax: Double,
    val pricePerKgMin: Double,
    val pricePerKgMax: Double,
    val bestMarkets: List<String>,
    val riskLevel: String // "Low", "Medium", "High"
)

object CropDatabase {
    val crops = listOf(
        CropBenchmark(
            id = "tomato",
            name = "Tomato",
            emoji = "🍅",
            durationDays = "90–120 days",
            season = "Kharif / Rabi",
            defaultSeedCostPerAcre = 6000.0,
            defaultFertilizerCostPerAcre = 14000.0,
            defaultLabourCostPerAcre = 24000.0,
            defaultIrrigationCostPerAcre = 6000.0,
            defaultMachineryCostPerAcre = 8000.0,
            defaultPestCostPerAcre = 7000.0,
            defaultOtherCostPerAcre = 5000.0,
            yieldKgPerAcreMin = 14000.0,
            yieldKgPerAcreMax = 18000.0,
            pricePerKgMin = 18.0,
            pricePerKgMax = 26.0,
            bestMarkets = listOf("Bengaluru", "Chennai", "Koyambedu", "Vellore"),
            riskLevel = "Medium"
        ),
        CropBenchmark(
            id = "onion",
            name = "Onion",
            emoji = "🧅",
            durationDays = "120–150 days",
            season = "Rabi / Late Kharif",
            defaultSeedCostPerAcre = 8000.0,
            defaultFertilizerCostPerAcre = 12000.0,
            defaultLabourCostPerAcre = 22000.0,
            defaultIrrigationCostPerAcre = 7000.0,
            defaultMachineryCostPerAcre = 6000.0,
            defaultPestCostPerAcre = 5000.0,
            defaultOtherCostPerAcre = 4000.0,
            yieldKgPerAcreMin = 10000.0,
            yieldKgPerAcreMax = 14000.0,
            pricePerKgMin = 22.0,
            pricePerKgMax = 32.0,
            bestMarkets = listOf("Lasalgaon", "Bengaluru", "Chennai", "Hosur"),
            riskLevel = "Medium"
        ),
        CropBenchmark(
            id = "potato",
            name = "Potato",
            emoji = "🥔",
            durationDays = "90–110 days",
            season = "Rabi",
            defaultSeedCostPerAcre = 18000.0,
            defaultFertilizerCostPerAcre = 16000.0,
            defaultLabourCostPerAcre = 18000.0,
            defaultIrrigationCostPerAcre = 8000.0,
            defaultMachineryCostPerAcre = 9000.0,
            defaultPestCostPerAcre = 6000.0,
            defaultOtherCostPerAcre = 5000.0,
            yieldKgPerAcreMin = 12000.0,
            yieldKgPerAcreMax = 16000.0,
            pricePerKgMin = 15.0,
            pricePerKgMax = 24.0,
            bestMarkets = listOf("Agra", "Chennai", "Bengaluru", "Koyambedu"),
            riskLevel = "Low"
        ),
        CropBenchmark(
            id = "millet",
            name = "Millet (Ragi)",
            emoji = "🌾",
            durationDays = "105–120 days",
            season = "Kharif",
            defaultSeedCostPerAcre = 2000.0,
            defaultFertilizerCostPerAcre = 6000.0,
            defaultLabourCostPerAcre = 12000.0,
            defaultIrrigationCostPerAcre = 3000.0,
            defaultMachineryCostPerAcre = 4000.0,
            defaultPestCostPerAcre = 2000.0,
            defaultOtherCostPerAcre = 2000.0,
            yieldKgPerAcreMin = 1500.0,
            yieldKgPerAcreMax = 2200.0,
            pricePerKgMin = 38.0,
            pricePerKgMax = 48.0,
            bestMarkets = listOf("Bengaluru", "Dharmapuri", "Mysuru", "Salem"),
            riskLevel = "Low"
        ),
        CropBenchmark(
            id = "chilli",
            name = "Green Chilli",
            emoji = "🌶️",
            durationDays = "120–160 days",
            season = "Kharif / Summer",
            defaultSeedCostPerAcre = 7000.0,
            defaultFertilizerCostPerAcre = 15000.0,
            defaultLabourCostPerAcre = 28000.0,
            defaultIrrigationCostPerAcre = 9000.0,
            defaultMachineryCostPerAcre = 6000.0,
            defaultPestCostPerAcre = 11000.0,
            defaultOtherCostPerAcre = 5000.0,
            yieldKgPerAcreMin = 6000.0,
            yieldKgPerAcreMax = 9000.0,
            pricePerKgMin = 35.0,
            pricePerKgMax = 65.0,
            bestMarkets = listOf("Guntur", "Bengaluru", "Chennai", "Madurai"),
            riskLevel = "High"
        ),
        CropBenchmark(
            id = "banana",
            name = "Banana (G9)",
            emoji = "🍌",
            durationDays = "300–360 days",
            season = "Year-round",
            defaultSeedCostPerAcre = 25000.0,
            defaultFertilizerCostPerAcre = 22000.0,
            defaultLabourCostPerAcre = 30000.0,
            defaultIrrigationCostPerAcre = 14000.0,
            defaultMachineryCostPerAcre = 10000.0,
            defaultPestCostPerAcre = 8000.0,
            defaultOtherCostPerAcre = 9000.0,
            yieldKgPerAcreMin = 28000.0,
            yieldKgPerAcreMax = 38000.0,
            pricePerKgMin = 12.0,
            pricePerKgMax = 20.0,
            bestMarkets = listOf("Tiruchirappalli", "Koyambedu", "Madurai", "Bengaluru"),
            riskLevel = "Medium"
        )
    )

    fun getById(id: String): CropBenchmark = crops.find { it.id == id } ?: crops.first()
}
