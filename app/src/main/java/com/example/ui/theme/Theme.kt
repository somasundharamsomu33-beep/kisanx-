package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = KisanGreenLight,
    onPrimary = Color.Black,
    primaryContainer = KisanGreenDark,
    onPrimaryContainer = KisanGreenContainer,
    secondary = HarvestAmber,
    onSecondary = Color.Black,
    background = Color(0xFF121812),
    surface = Color(0xFF1B221B),
    onBackground = Color(0xFFE2E8E2),
    onSurface = Color(0xFFE2E8E2),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = KisanGreen,
    onPrimary = Color.White,
    primaryContainer = KisanGreenContainer,
    onPrimaryContainer = KisanGreenDark,
    secondary = HarvestGold,
    onSecondary = Color.White,
    background = SoftBackground,
    surface = SoftSurface,
    surfaceVariant = SurfaceVariantColor,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

