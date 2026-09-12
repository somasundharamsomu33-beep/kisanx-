package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.calculator.FarmProfitCalculatorScreen
import com.example.ui.maxprofit.MaxProfitModeScreen
import com.example.ui.theme.MyApplicationTheme

enum class Screen {
    CALCULATOR,
    MAX_PROFIT
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var currentScreen by remember { mutableStateOf(Screen.CALCULATOR) }
                var selectedCropName by remember { mutableStateOf("Tomato") }
                var targetQuantityTonnes by remember { mutableDoubleStateOf(5.0) }

                when (currentScreen) {
                    Screen.CALCULATOR -> {
                        FarmProfitCalculatorScreen(
                            onNavigateToMaxProfitMode = { crop, tonnes ->
                                selectedCropName = crop
                                targetQuantityTonnes = tonnes
                                currentScreen = Screen.MAX_PROFIT
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Screen.MAX_PROFIT -> {
                        MaxProfitModeScreen(
                            cropName = selectedCropName,
                            quantityTonnes = targetQuantityTonnes,
                            onBack = { currentScreen = Screen.CALCULATOR },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

