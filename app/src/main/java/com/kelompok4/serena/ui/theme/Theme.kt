package com.kelompok4.serena.ui.theme

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

// Skema warna untuk Dark Mode
private val DarkColorScheme = darkColorScheme(
    primary = Primary300,
    secondary = Secondary300,      // Menggunakan varian secondary yang lebih terang
    tertiary = TertiaryBlue300,    // Menggunakan varian tertiary yang lebih terang
    background = Primary900,
    surface = Primary900,
    error = TertiaryRed300,        // Warna error untuk dark mode
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Primary50,
    onSurface = Primary50,
    onError = Color.Black
)

// Skema warna untuk Light Mode
private val LightColorScheme = lightColorScheme(
    primary = Primary500,
    secondary = Secondary500,
    tertiary = TertiaryBlue500,
    background = Color.White,
    surface = Primary50,
    error = TertiaryRed500,        // Warna error untuk light mode
    onPrimary = Color.White,
    onSecondary = Color.Black,     // Teks hitam di atas warna secondary (kuning)
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

@Composable
fun ProyekakhirpapbTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}