package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.kelompok4.serena.data.Mood
import com.kelompok4.serena.data.MoodDataManager
import com.kelompok4.serena.data.MoodStats
import com.kelompok4.serena.data.MoodTypes
import com.kelompok4.serena.ui.navigation.Routes
import com.kelompok4.serena.ui.theme.*

// --- 1. STATEFUL COMPOSABLE (Logika Data & Navigasi) ---
@Composable
fun MoodRecapScreen(
    navController: NavHostController,
    userEmail: String
) {
    val context = LocalContext.current

    // LOAD DATA: Menggunakan getLatestMood agar konsisten dengan Home
    val todayMood by produceState<Mood?>(initialValue = null, key1 = userEmail) {
        value = MoodDataManager.getLatestMood(context, userEmail)
    }

    val stats by produceState(initialValue = MoodStats(), key1 = userEmail) {
        value = MoodDataManager.getMoodStats(context, userEmail, 7)
    }

    MoodRecapContent(
        todayMood = todayMood,
        stats = stats,
        onBackClick = {
            // PERBAIKAN: Gunakan popBackStack untuk kembali ke Home secara natural.
            // Karena SaveMoodScreen sudah di-pop sebelumnya, stack di bawah ini pasti Home.
            // Ini memicu ON_RESUME di Home tanpa memaksa restoreState yang mungkin kadaluarsa.
            navController.popBackStack(Routes.HOME, inclusive = false)
        },
        onHistoryClick = { navController.navigate("mood_history/$userEmail") },
        onSelfCareClick = {
            navController.navigate(Routes.SELF_CARE) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

// --- 2. STATELESS COMPOSABLE (Tampilan UI) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodRecapContent(
    todayMood: Mood?,
    stats: MoodStats,
    onBackClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSelfCareClick: () -> Unit
) {
    // Menentukan warna background
    val moodColor = remember(todayMood) {
        todayMood?.let {
            try {
                Color(android.graphics.Color.parseColor(MoodTypes.getMoodColor(it.moodName)))
            } catch (e: Exception) {
                Secondary300
            }
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
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali ke Beranda",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp)) // Penyeimbang layout title center
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // KONTEN UTAMA (Scrollable)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- Main Mood Card ---
                if (todayMood != null) {
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
                                text = todayMood.moodEmoji,
                                fontSize = 100.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = todayMood.moodName,
                                style = AppTypography.H3.bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = when (todayMood.moodName) {
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
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(
                            modifier = Modifier.padding(32.dp).fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Belum ada mood yang tercatat hari ini",
                                style = AppTypography.Body1.medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // --- Recommendation Card ---
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onHistoryClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Primary50)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Primary500),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎯", fontSize = 32.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Yuk, cek Riwayat Mood kamu!", style = AppTypography.Body1.bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Lihat pola perasaanmu dalam seminggu terakhir.",
                                style = AppTypography.Subtitle2.regular,
                                color = GrayText
                            )
                        }
                    }
                }

                // --- Stats Section ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Statistik Mood (7 Hari)", style = AppTypography.H6.bold)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Mood Icons Row
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            listOf("😊", "😢", "😐", "😠", "😭").forEach { emoji ->
                                Text(text = emoji, fontSize = 24.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Bar Chart
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val maxValue = maxOf(stats.gembirCount, stats.sedihCount, stats.netralCount, stats.marahCount, stats.depresiCount, 1)

                            listOf(
                                stats.gembirCount to Secondary500,
                                stats.sedihCount to TertiaryBlue500,
                                stats.netralCount to Primary300,
                                stats.marahCount to TertiaryRed500,
                                stats.depresiCount to Primary700
                            ).forEach { (count, color) ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    val heightRatio = if (maxValue > 0) count.toFloat() / maxValue else 0f
                                    Box(
                                        modifier = Modifier
                                            .width(40.dp)
                                            .fillMaxHeight(heightRatio.coerceIn(0.05f, 1f))
                                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                            .background(color)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // TOMBOL FIXED (SelfCare) - Opsional, jika ingin navigasi ke SelfCare
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onSelfCareClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary500),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Lihat SelfCare", style = AppTypography.Subtitle2.bold)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun MoodRecapScreenPreview() {
    ProyekakhirpapbTheme {
        val dummyMood = Mood(moodName = MoodTypes.GEMBIRA, moodEmoji = "😊", userEmail = "test@example.com")
        val dummyStats = MoodStats(10, 5, 2, 2, 1, 0)
        MoodRecapContent(dummyMood, dummyStats, {}, {}, {})
    }
}