package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodHistoryScreen(
    navController: NavHostController,
    userEmail: String
) {
    val context = LocalContext.current
    var selectedPeriod by remember { mutableStateOf("Week") }
    val moods = remember(selectedPeriod) {
        MoodDataManager.getMoodsByUser(context, userEmail)
    }
    val stats = remember(selectedPeriod) {
        val days = when (selectedPeriod) {
            "Day" -> 1
            "Week" -> 7
            "Month" -> 30
            "Year" -> 365
            else -> 7
        }
        MoodDataManager.getMoodStats(context, userEmail, days)
    }

    val weeklyData = remember {
        MoodDataManager.getWeeklyMoodData(context, userEmail)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Riwayat Mood",
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
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Period Selector
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Day", "Week", "Month", "Year").forEach { period ->
                        FilterChip(
                            selected = selectedPeriod == period,
                            onClick = { selectedPeriod = period },
                            label = { Text(period) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Primary500,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Statistics Chart Card
            item {
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

                        // Emoji Labels
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("😊", "😢", "😐", "😠", "😭").forEach { emoji ->
                                Text(text = emoji, fontSize = 20.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Bar Chart
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
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
                                Triple(stats.gembirCount, Secondary500, MoodTypes.GEMBIRA),
                                Triple(stats.sedihCount, TertiaryBlue500, MoodTypes.SEDIH),
                                Triple(stats.netralCount, Primary300, MoodTypes.NETRAL),
                                Triple(stats.marahCount, TertiaryRed500, MoodTypes.MARAH),
                                Triple(stats.depresiCount, Primary700, MoodTypes.DEPRESI)
                            ).forEach { (count, color, label) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    if (count > 0) {
                                        Surface(
                                            modifier = Modifier
                                                .width(40.dp)
                                                .padding(bottom = 4.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            color = color.copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = label,
                                                style = AppTypography.Button.medium,
                                                color = color,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                                fontSize = 10.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }

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
                        }
                    }
                }
            }

            // Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Primary50),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Kesimpulan",
                            style = AppTypography.H6.bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when {
                                stats.totalMoods == 0 -> "Belum ada data mood. Yuk mulai catat mood kamu!"
                                stats.gembirPercentage >= 0.5f -> "Mood kamu minggu ini naik turun, tapi itu hal yang sangat wajar, kok. Di awal minggu, mood kamu lagi cukup stabil. Namun, ada penurunan cukup tajam di tengah dengan perasaan gembira, Namun, ada penurunan cukup tajam dengan perasaan gembira. Namun, ada penurunan cukup..."
                                stats.depresiPercentage >= 0.3f -> "Sepertinya minggu ini cukup berat ya. Jangan ragu untuk berbicara dengan seseorang atau menggunakan fitur konseling kami."
                                else -> "Mood kamu cukup stabil minggu ini. Pertahankan ya!"
                            },
                            style = AppTypography.Subtitle2.regular,
                            color = GrayText
                        )

                        if (stats.totalMoods > 0) {
                            Spacer(modifier = Modifier.height(12.dp))
                            TextButton(
                                onClick = { /* Navigate to full history */ }
                            ) {
                                Text(
                                    text = "Lihat Semua",
                                    style = AppTypography.Body1.medium,
                                    color = Primary500
                                )
                            }
                        }
                    }
                }
            }

            // Recommendation Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Primary500)
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
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "💚",
                                fontSize = 32.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Serena punya rekomendasi buat kamu!",
                                style = AppTypography.Body1.bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Coba kegiatan sederhana yang bisa bantu kamu merasa lebih tenang dan fokus pada fitur SelfCare",
                                style = AppTypography.Subtitle2.regular,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            // Recent Moods List (if any)
            if (moods.isNotEmpty()) {
                item {
                    Text(
                        text = "Riwayat Terakhir",
                        style = AppTypography.H6.bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(moods.take(10)) { mood ->
                    MoodHistoryItem(mood = mood)
                }
            }
        }
    }
}

@Composable
fun MoodHistoryItem(mood: com.kelompok4.serena.data.Mood) {
    val dateStr = remember(mood.date) {
        val sdf = SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id", "ID"))
        sdf.format(Date(mood.date))
    }

    val moodColor = Color(android.graphics.Color.parseColor(MoodTypes.getMoodColor(mood.moodName)))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(moodColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mood.moodEmoji,
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mood.moodName,
                    style = AppTypography.Body1.bold
                )
                Text(
                    text = dateStr,
                    style = AppTypography.Button.regular,
                    color = GrayText
                )
            }
        }
    }
}