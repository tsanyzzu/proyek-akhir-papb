package com.kelompok4.serena.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kelompok4.serena.R
import kotlinx.coroutines.delay
import com.kelompok4.serena.ui.theme.*

@Composable
fun SplashScreen(
    onNavigate: () -> Unit
) {
    var showHands by remember { mutableStateOf(false) }
    var showHeart by remember { mutableStateOf(false) }
    var backgroundIsWhite by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        // Ganti nilai Color() dengan variabel dari design system
        targetValue = if (backgroundIsWhite) Color.White else Primary500,
        label = "backgroundColor"
    )

    // Urutan animasi
    LaunchedEffect(Unit) {
        delay(500)      // Layar hijau kosong
        showHands = true
        delay(800)      // Muncul tangan
        showHeart = true
        delay(1000)     // Hati animasi
        backgroundIsWhite = true
        delay(1000)     // Background jadi putih
        onNavigate()    // Navigasi ke layar berikutnya
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (showHands) {
                Image(
                    painter = painterResource(id = R.drawable.hands_logo),
                    contentDescription = "Hands Logo",
                    modifier = Modifier.size(width = 303.dp, height = 97.dp)
                )
            }

            val heartScale by animateFloatAsState(
                targetValue = if (showHeart) 1f else 0f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                label = "heartScale"
            )

            val heartAlpha by animateFloatAsState(
                targetValue = if (showHeart) 1f else 0f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                label = "heartAlpha"
            )

            Image(
                painter = painterResource(id = R.drawable.heart_logo),
                contentDescription = "Heart Logo",
                modifier = Modifier
                    .size(110.dp)
                    .offset(y = (-60).dp)
                    .scale(heartScale)
                    .alpha(heartAlpha)
            )
        }
    }
}