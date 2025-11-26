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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kelompok4.serena.data.Mood
import com.kelompok4.serena.data.MoodDataManager
import com.kelompok4.serena.data.MoodStats
import com.kelompok4.serena.data.MoodTypes
import com.kelompok4.serena.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

// --- 1. STATEFUL COMPOSABLE (Logika Data) ---
@Composable
fun MoodHistoryScreen(
    navController: NavHostController,
    userEmail: String
) {
    val context = LocalContext.current
    var selectedPeriod by remember { mutableStateOf("Week") }

    val moods = remember(selectedPeriod) {
        MoodDataManager.getMoods(context, userEmail)
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

    // Panggil UI Content dan kirim datanya
    MoodHistoryContent(
        moods = moods,
        stats = stats,
        selectedPeriod = selectedPeriod,
        onPeriodChange = { selectedPeriod = it },
        onBackClick = { navController.navigateUp() }
    )
}

// --- 2. STATELESS COMPOSABLE (Tampilan UI) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodHistoryContent(
    moods: List<Mood>,
    stats: MoodStats,
    selectedPeriod: String,
    onPeriodChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
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
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
                            onClick = { onPeriodChange(period) },
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
                                                text = count.toString(),
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
                                            .fillMaxHeight(heightRatio.coerceIn(0.02f, 1f))
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
                                stats.gembirPercentage >= 0.5f -> "Wah, mood kamu dominan positif! Pertahankan energi baik ini ya."
                                stats.depresiPercentage >= 0.3f || stats.marahPercentage >= 0.3f -> "Sepertinya minggu ini cukup berat. Jangan ragu untuk istirahat atau cerita ke orang terdekat."
                                else -> "Mood kamu cukup bervariasi. Ingat, semua perasaan itu valid kok."
                            },
                            style = AppTypography.Subtitle2.regular,
                            color = GrayText
                        )
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
                                text = "Rekomendasi Serena",
                                style = AppTypography.Body1.bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Coba latihan pernapasan atau dengarkan musik relaksasi di menu Self-Care.",
                                style = AppTypography.Subtitle2.regular,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            // Recent Moods List
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
fun MoodHistoryItem(mood: Mood) {
    val dateStr = remember(mood.date) {
        val sdf = SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id", "ID"))
        sdf.format(Date(mood.date))
    }

    val moodColor = try {
        Color(android.graphics.Color.parseColor(MoodTypes.getMoodColor(mood.moodName)))
    } catch (e: Exception) {
        Primary500
    }

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

// --- 3. PREVIEW ---
@Preview(showBackground = true, widthDp = 360, heightDp = 1200)
@Composable
fun MoodHistoryScreenPreview() {
    ProyekakhirpapbTheme {
        // Data Dummy untuk Preview
        val dummyStats = MoodStats(
            totalMoods = 10,
            gembirCount = 4,
            sedihCount = 2,
            netralCount = 3,
            marahCount = 0,
            depresiCount = 1
        )

        val dummyMoods = listOf(
            Mood(moodName = MoodTypes.GEMBIRA, moodEmoji = "😊", userEmail = "test", date = System.currentTimeMillis()),
            Mood(moodName = MoodTypes.SEDIH, moodEmoji = "😢", userEmail = "test", date = System.currentTimeMillis() - 86400000),
            Mood(moodName = MoodTypes.NETRAL, moodEmoji = "😐", userEmail = "test", date = System.currentTimeMillis() - 172800000)
        )

        // Menggunakan MoodHistoryContent (Stateless) agar bisa diisi data dummy
        MoodHistoryContent(
            moods = dummyMoods,
            stats = dummyStats,
            selectedPeriod = "Week",
            onPeriodChange = {},
            onBackClick = {}
        )
    }
}