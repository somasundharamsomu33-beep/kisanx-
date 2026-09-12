package com.example.ui.calculator

import androidx.lifecycle.ViewModel
import com.example.model.CropBenchmark
import com.example.model.CropDatabase
import com.example.model.FarmProfitCalculationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CalculatorUiState(
    val selectedCrop: CropBenchmark = CropDatabase.crops.first(),
    val landSizeInput: String = "1.0",
    val seedCostInput: String = "",
    val fertilizerCostInput: String = "",
    val labourCostInput: String = "",
    val irrigationCostInput: String = "",
    val machineryCostInput: String = "",
    val pestCostInput: String = "",
    val otherCostInput: String = "",
    val customExpectedPriceMinInput: String = "",
    val customExpectedPriceMaxInput: String = "",
    val calculationResult: FarmProfitCalculationResult? = null,
    val showDisclaimerDialog: Boolean = false,
    val activeExpenseCategoryIndex: Int = 0
)

class FarmProfitCalculatorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    init {
        // Initialize default expense inputs based on default crop & 1 acre
        applyBenchmarkExpensesForCrop(CropDatabase.crops.first(), 1.0)
    }

    fun selectCrop(crop: CropBenchmark) {
        val land = _uiState.value.landSizeInput.toDoubleOrNull() ?: 1.0
        _uiState.update { it.copy(selectedCrop = crop) }
        applyBenchmarkExpensesForCrop(crop, land)
    }

    fun updateLandSize(input: String) {
        val land = input.toDoubleOrNull()
        _uiState.update { it.copy(landSizeInput = input) }
        if (land != null && land > 0) {
            applyBenchmarkExpensesForCrop(_uiState.value.selectedCrop, land)
        }
    }

    fun updateSeedCost(input: String) {
        _uiState.update { it.copy(seedCostInput = input) }
        recalculate()
    }

    fun updateFertilizerCost(input: String) {
        _uiState.update { it.copy(fertilizerCostInput = input) }
        recalculate()
    }

    fun updateLabourCost(input: String) {
        _uiState.update { it.copy(labourCostInput = input) }
        recalculate()
    }

    fun updateIrrigationCost(input: String) {
        _uiState.update { it.copy(irrigationCostInput = input) }
        recalculate()
    }

    fun updateMachineryCost(input: String) {
        _uiState.update { it.copy(machineryCostInput = input) }
        recalculate()
    }

    fun updatePestCost(input: String) {
        _uiState.update { it.copy(pestCostInput = input) }
        recalculate()
    }

    fun updateOtherCost(input: String) {
        _uiState.update { it.copy(otherCostInput = input) }
        recalculate()
    }

    fun updateExpectedPriceMin(input: String) {
        _uiState.update { it.copy(customExpectedPriceMinInput = input) }
        recalculate()
    }

    fun updateExpectedPriceMax(input: String) {
        _uiState.update { it.copy(customExpectedPriceMaxInput = input) }
        recalculate()
    }

    fun resetToDefaults() {
        val land = _uiState.value.landSizeInput.toDoubleOrNull() ?: 1.0
        applyBenchmarkExpensesForCrop(_uiState.value.selectedCrop, land)
    }

    fun toggleDisclaimer(show: Boolean) {
        _uiState.update { it.copy(showDisclaimerDialog = show) }
    }

    private fun applyBenchmarkExpensesForCrop(crop: CropBenchmark, landSize: Double) {
        val safeLand = if (landSize > 0) landSize else 1.0
        _uiState.update {
            it.copy(
                seedCostInput = (crop.defaultSeedCostPerAcre * safeLand).toInt().toString(),
                fertilizerCostInput = (crop.defaultFertilizerCostPerAcre * safeLand).toInt().toString(),
                labourCostInput = (crop.defaultLabourCostPerAcre * safeLand).toInt().toString(),
                irrigationCostInput = (crop.defaultIrrigationCostPerAcre * safeLand).toInt().toString(),
                machineryCostInput = (crop.defaultMachineryCostPerAcre * safeLand).toInt().toString(),
                pestCostInput = (crop.defaultPestCostPerAcre * safeLand).toInt().toString(),
                otherCostInput = (crop.defaultOtherCostPerAcre * safeLand).toInt().toString(),
                customExpectedPriceMinInput = crop.pricePerKgMin.toInt().toString(),
                customExpectedPriceMaxInput = crop.pricePerKgMax.toInt().toString()
            )
        }
        recalculate()
    }

    fun recalculate() {
        val state = _uiState.value
        val crop = state.selectedCrop
        val land = state.landSizeInput.toDoubleOrNull() ?: 0.0

        val seed = state.seedCostInput.toDoubleOrNull() ?: 0.0
        val fertilizer = state.fertilizerCostInput.toDoubleOrNull() ?: 0.0
        val labour = state.labourCostInput.toDoubleOrNull() ?: 0.0
        val irrigation = state.irrigationCostInput.toDoubleOrNull() ?: 0.0
        val machinery = state.machineryCostInput.toDoubleOrNull() ?: 0.0
        val pest = state.pestCostInput.toDoubleOrNull() ?: 0.0
        val other = state.otherCostInput.toDoubleOrNull() ?: 0.0

        val totalInvestment = seed + fertilizer + labour + irrigation + machinery + pest + other

        val safeLand = if (land > 0) land else 1.0
        val yieldMin = crop.yieldKgPerAcreMin * safeLand
        val yieldMax = crop.yieldKgPerAcreMax * safeLand
        val avgYield = (yieldMin + yieldMax) / 2.0

        val priceMin = state.customExpectedPriceMinInput.toDoubleOrNull() ?: crop.pricePerKgMin
        val priceMax = state.customExpectedPriceMaxInput.toDoubleOrNull() ?: crop.pricePerKgMax

        val revenueMin = yieldMin * priceMin
        val revenueMax = yieldMax * priceMax

        val profitMin = revenueMin - totalInvestment
        val profitMax = revenueMax - totalInvestment

        val roiMin = if (totalInvestment > 0) (profitMin / totalInvestment) * 100.0 else 0.0
        val roiMax = if (totalInvestment > 0) (profitMax / totalInvestment) * 100.0 else 0.0

        val breakEvenPrice = if (avgYield > 0) totalInvestment / avgYield else 0.0
        val costPerKg = if (avgYield > 0) totalInvestment / avgYield else 0.0
        val costPerAcre = if (safeLand > 0) totalInvestment / safeLand else 0.0
        val profitPerAcreMin = if (safeLand > 0) profitMin / safeLand else 0.0
        val profitPerAcreMax = if (safeLand > 0) profitMax / safeLand else 0.0

        val result = FarmProfitCalculationResult(
            landSizeAcre = land,
            cropName = crop.name,
            cropEmoji = crop.emoji,
            totalInvestment = totalInvestment,
            seedCost = seed,
            fertilizerCost = fertilizer,
            labourCost = labour,
            irrigationCost = irrigation,
            machineryCost = machinery,
            pestCost = pest,
            otherCost = other,
            expectedYieldKgMin = yieldMin,
            expectedYieldKgMax = yieldMax,
            expectedPricePerKgMin = priceMin,
            expectedPricePerKgMax = priceMax,
            expectedRevenueMin = revenueMin,
            expectedRevenueMax = revenueMax,
            estimatedProfitMin = profitMin,
            estimatedProfitMax = profitMax,
            roiPercentageMin = roiMin,
            roiPercentageMax = roiMax,
            breakEvenPricePerKg = breakEvenPrice,
            costPerKgExpected = costPerKg,
            costPerAcre = costPerAcre,
            profitPerAcreMin = profitPerAcreMin,
            profitPerAcreMax = profitPerAcreMax
        )

        _uiState.update { it.copy(calculationResult = result) }
    }
}
