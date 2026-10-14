package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary & Accent Colors
val NeonPurple = Color(0xFF8B5CF6)
val DeepPurple = Color(0xFF6D28D9)
val NeonCyan = Color(0xFF06B6D4)
val ElectricBlue = Color(0xFF3B82F6)
val HotPink = Color(0xFFEC4899)
val GoldYellow = Color(0xFFF59E0B)
val SuccessGreen = Color(0xFF10B981)
val ErrorRed = Color(0xFFEF4444)

// Surface & Dark Background Tones
val DarkBackground = Color(0xFF090614)
val DarkSurface = Color(0xFF120D26)
val DarkSurfaceVariant = Color(0xFF1E173D)
val DarkSurfaceHighlight = Color(0xFF2C2256)
val DarkOutline = Color(0xFF3B3167)

// Text Colors
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextTertiary = Color(0xFF64748B)

// Gradients
val PrimaryGradient = Brush.horizontalGradient(
    colors = listOf(NeonPurple, ElectricBlue)
)

val AccentGradient = Brush.linearGradient(
    colors = listOf(NeonCyan, NeonPurple)
)

val PinkPurpleGradient = Brush.linearGradient(
    colors = listOf(HotPink, NeonPurple)
)

val GoldCreditGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFF59E0B), Color(0xFFFBBF24))
)

val DarkCardGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF1A133B), Color(0xFF100C24))
)
