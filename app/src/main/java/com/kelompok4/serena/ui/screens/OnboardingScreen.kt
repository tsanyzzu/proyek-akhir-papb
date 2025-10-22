package com.kelompok4.serena.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompok4.serena.R
import com.kelompok4.serena.ui.components.AppButton
import com.kelompok4.serena.ui.components.ButtonType
import com.kelompok4.serena.ui.theme.*

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var currentPage by remember { mutableIntStateOf(0) }
    
    val onboardingData = listOf(
        OnboardingData(
            image = R.drawable.onboarding_1,
            title = "Konsultasi dengan Ahli",
            description = "Dapatkan bimbingan dari para ahli untuk membantu mengatasi permasalahan dan meningkatkan kesejahteraan mental Anda"
        ),
        OnboardingData(
            image = R.drawable.onboarding_2,
            title = "Jurnal Pribadi",
            description = "Catat perasaan dan pengalaman harian Anda untuk memahami perkembangan emosi dan meningkatkan kesejahteraan diri."
        ),
        OnboardingData(
            image = R.drawable.onboarding_3,
            title = "Lacak Pola Tidur",
            description = "Pantau kualitas tidur Anda dan dapatkan rekomendasi untuk meningkatkan pola tidur yang lebih sehat."
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Background dengan wave hijau
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Primary50, Primary100)
                    )
                )
        )
        
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onFinish,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Primary500
                    )
                ) {
                    Text(
                        text = "Lewati",
                        style = AppTypography.Subtitle2.medium
                    )
                }
            }
            
            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Image
                Image(
                    painter = painterResource(id = onboardingData[currentPage].image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Fit
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Page Indicator
                PageIndicator(
                    currentPage = currentPage,
                    totalPages = onboardingData.size
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Title
                Text(
                    text = onboardingData[currentPage].title,
                    style = AppTypography.H3.bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Description
                Text(
                    text = onboardingData[currentPage].description,
                    style = AppTypography.Body1.regular,
                    color = Color.Black.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Navigation Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = if (currentPage == 0) {
                        Arrangement.End
                    } else {
                        Arrangement.SpaceBetween
                    }
                ) {
                    if (currentPage > 0) {
                        AppButton(
                            text = "Kembali",
                            onClick = { currentPage-- },
                            modifier = Modifier.weight(1f),
                            buttonType = ButtonType.SECONDARY
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    
                    AppButton(
                        text = if (currentPage == onboardingData.size - 1) "Mulai Sekarang" else "Lanjutkan",
                        onClick = {
                            if (currentPage == onboardingData.size - 1) {
                                onFinish()
                            } else {
                                currentPage++
                            }
                        },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.PRIMARY
                    )
                }
            }
        }
    }
}

@Composable
fun PageIndicator(
    currentPage: Int,
    totalPages: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalPages) { index ->
            // Animasi untuk lebar indikator
            val animatedWidth by animateDpAsState(
                targetValue = if (index == currentPage) 24.dp else 8.dp,
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
                label = "indicatorWidth"
            )

            // Animasi untuk warna indikator
            val animatedColor by animateColorAsState(
                targetValue = if (index == currentPage) Primary500 else Color.Gray.copy(alpha = 0.3f),
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
                label = "indicatorColor"
            )

            Box(
                modifier = Modifier
                    .width(animatedWidth)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(animatedColor)
            )
        }
    }
}


data class OnboardingData(
    val image: Int,
    val title: String,
    val description: String
)