package com.example.model

import java.text.NumberFormat
import java.util.Locale

object IndianCurrencyFormatter {
    private val indianLocale = Locale("en", "IN")
    private val formatter = NumberFormat.getCurrencyInstance(indianLocale).apply {
        maximumFractionDigits = 0
    }

    fun formatRupees(amount: Double): String {
        return try {
            "₹" + NumberFormat.getNumberInstance(indianLocale).format(amount.toLong())
        } catch (e: Exception) {
            "₹${amount.toLong()}"
        }
    }

    /**
     * Formats into Lakhs or Thousands (e.g. ₹3.24L or ₹85K)
     */
    fun formatCompactRupees(amount: Double): String {
        val absVal = kotlin.math.abs(amount)
        val prefix = if (amount < 0) "-₹" else "₹"
        return when {
            absVal >= 100_000 -> {
                val lakhs = absVal / 100_000.0
                String.format(Locale.US, "%s%.2fL", prefix, lakhs)
            }
            absVal >= 1_000 -> {
                val thousands = absVal / 1_000.0
                String.format(Locale.US, "%s%.1fK", prefix, thousands)
            }
            else -> {
                String.format(Locale.US, "%s%.0f", prefix, absVal)
            }
        }
    }

    fun formatKg(kg: Double): String {
        return if (kg >= 1000) {
            val tonnes = kg / 1000.0
            if (tonnes % 1.0 == 0.0) {
                "${tonnes.toInt()} Tonnes (${NumberFormat.getNumberInstance(indianLocale).format(kg.toLong())} kg)"
            } else {
                String.format(Locale.US, "%.1f Tonnes (%s kg)", tonnes, NumberFormat.getNumberInstance(indianLocale).format(kg.toLong()))
            }
        } else {
            "${kg.toInt()} kg"
        }
    }
}
