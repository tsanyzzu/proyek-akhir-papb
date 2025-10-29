package com.kelompok4.serena.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok4.serena.ui.components.AppButton
import com.kelompok4.serena.ui.components.ButtonType
import com.kelompok4.serena.ui.theme.AppTypography
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun SuccessProfileScreen(navController: NavController) {
    var start by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (start) 1f else 0f)
    LaunchedEffect(Unit) { start = true }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )
        Spacer(Modifier.height(24.dp))
        Text("Yeyy berhasil!", style = AppTypography.H4.bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Masukanmu membantu kami meningkatkan layanan. Semoga sesi ini bermanfaat.",
            style = AppTypography.Subtitle2.regular,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(32.dp))
        AppButton(
            text = "Kembali Ke Profil",
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.PRIMARY
        )
    }
}