package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kelompok4.serena.data.MoodDataManager
import com.kelompok4.serena.data.MoodTypes
import com.kelompok4.serena.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodRecapScreen(
    navController: NavHostController,
    userEmail: String
) {
    val context = LocalContext.current
    val todayMood = remember { MoodDataManager.getTodayMood(context, userEmail) }
    val stats = remember { MoodDataManager.getMoodStats(context, userEmail, 7) }

    val moodColor = remember(todayMood) {
        todayMood?.let {
            Color(android.graphics.Color.parseColor(MoodTypes.getMoodColor(it.moodName)))
        } ?: Secondary300
    }

    Scaffold(
        containerColor = moodColor.copy(alpha = 0.2f),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Rekap Mood",
                        style = AppTypography.H6.bold,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Mood Card
            todayMood?.let { mood ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = moodColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = mood.moodEmoji,
                            fontSize = 100.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = mood.moodName,
                            style = AppTypography.H3.bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when (mood.moodName) {
                                MoodTypes.GEMBIRA -> "Mood kamu lagi oke banget! Yuk lakukan hal-hal yang kamu suka!"
                                MoodTypes.SEDIH -> "Gapapa merasa sedih, itu wajar kok. Yuk cerita ke Serena!"
                                MoodTypes.NETRAL -> "Hari yang biasa aja ya? Semoga besok lebih baik!"
                                MoodTypes.MARAH -> "Tarik nafas dulu yuk. Serena siap dengerin kamu."
                                MoodTypes.DEPRESI -> "Kamu nggak sendirian. Serena ada buat kamu."
                                else -> "Terima kasih sudah berbagi perasaanmu!"
                            },
                            style = AppTypography.Subtitle2.medium,
                            color = Primary700,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Recommendation Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Primary50)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Primary500),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎯",
                            fontSize = 32.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Yuk, cek Riwayat Mood kamu!",
                            style = AppTypography.Body1.bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Coba kegiatan sederhana yang bisa bantu kamu merasa lebih tenang dan fokus pada fitur SelfCare",
                            style = AppTypography.Subtitle2.regular,
                            color = GrayText
                        )
                    }
                }
            }

            // Stats Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Statistik Mood",
                        style = AppTypography.H6.bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Mood Icons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("😊", "😢", "😐", "😠", "😭").forEach { emoji ->
                            Text(text = emoji, fontSize = 24.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bar Chart Placeholder
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val maxValue = maxOf(
                            stats.gembirCount,
                            stats.sedihCount,
                            stats.netralCount,
                            stats.marahCount,
                            stats.depresiCount,
                            1
                        )

                        listOf(
                            stats.gembirCount to Secondary500,
                            stats.sedihCount to TertiaryBlue500,
                            stats.netralCount to Primary300,
                            stats.marahCount to TertiaryRed500,
                            stats.depresiCount to Primary700
                        ).forEach { (count, color) ->
                            val heightRatio = if (maxValue > 0) count.toFloat() / maxValue else 0f
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .fillMaxHeight(heightRatio.coerceIn(0.1f, 1f))
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(color)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Day Labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("😊", "😢", "😐", "😠", "😭").forEach { _ ->
                            Text(
                                text = "",
                                style = AppTypography.Button.regular,
                                color = GrayText,
                                modifier = Modifier.width(40.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate("mood_history/$userEmail")
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary500
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Lihat SelfCare", style = AppTypography.Subtitle2.bold)
                }
            }
        }
    }
}