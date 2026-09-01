package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TikTokDarkColorScheme = darkColorScheme(
    primary = TikTokRed,
    secondary = TikTokCyan,
    tertiary = TikTokCoinGold,
    background = TikTokBlack,
    surface = TikTokDarkBg,
    surfaceVariant = TikTokSheetBg,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = TikTokLightGray
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TikTokDarkColorScheme,
        typography = Typography,
        content = content
    )
}
