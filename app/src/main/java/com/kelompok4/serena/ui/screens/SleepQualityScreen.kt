package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Tambahkan import ini
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kelompok4.serena.ui.theme.*
import com.kelompok4.serena.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepQualityScreen(navController: NavHostController) {
    Scaffold(
        containerColor = Primary50,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kualitas Tidur",
                        style = AppTypography.H6.bold,
                        color = Color.Black
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary50
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- CARD 1: Summary (Score & Rata-rata) ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(100.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = 1f,
                            strokeWidth = 8.dp,
                            color = Primary100,
                            modifier = Modifier.fillMaxSize()
                        )
                        CircularProgressIndicator(
                            progress = 0.8f,
                            strokeWidth = 8.dp,
                            color = Primary500,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(
                            text = "80",
                            style = AppTypography.H2.bold,
                            color = Primary700
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "07 j 04 m",
                            style = AppTypography.H4.bold
                        )
                        Text(
                            text = "Rata-rata waktu tidur",
                            style = AppTypography.Subtitle2.regular,
                            color = GrayText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = "80%",
                                    style = AppTypography.Body1.bold
                                )
                                Text(
                                    text = "Normal",
                                    style = AppTypography.Button.regular,
                                    color = GrayText
                                )
                            }
                            Column {
                                Text(
                                    text = "20%",
                                    style = AppTypography.Body1.bold
                                )
                                Text(
                                    text = "Insomnia",
                                    style = AppTypography.Button.regular,
                                    color = GrayText
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Kualitas tidur Anda sehat",
                            style = AppTypography.Body1.medium,
                            color = Primary500
                        )
                    }
                }
            }

            // --- CARD 2: Statistik Tidur (Bar Chart) ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Statistik Tidur",
                        style = AppTypography.H5.bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val barData = listOf(6f, 8f, 7f, 9f, 8f, 6f, 7f)
                    val maxHours = 12f

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        barData.forEach { hours ->
                            val barHeightRatio = hours / maxHours
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .fillMaxHeight(barHeightRatio)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(Primary500)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        days.forEach { day ->
                            Text(
                                text = day,
                                style = AppTypography.Button.regular,
                                color = GrayText
                            )
                        }
                    }
                }
            }

            // --- CARD 3: Weekly Improvement ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(60.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = 1f,
                            strokeWidth = 6.dp,
                            color = Primary100,
                            modifier = Modifier.fillMaxSize()
                        )
                        CircularProgressIndicator(
                            progress = 0.76f,
                            strokeWidth = 6.dp,
                            color = Primary500,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(
                            text = "76%",
                            style = AppTypography.Body1.bold,
                            color = Primary700
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Kualitas tidurmu naik 6% dari minggu lalu",
                        style = AppTypography.Body1.medium,
                        color = Color.Black
                    )
                }
            }

            // --- SECTION: Alarm Sehat ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Alarm Sehat",
                    style = AppTypography.H5.bold
                )
                IconButton(onClick = { /* TODO: handle add alarm */ }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Tambah Alarm",
                        tint = Primary500
                    )
                }
            }

            // --- CARD 4: Alarm Details ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Primary50),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Alarm,
                            contentDescription = null,
                            tint = Primary500,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "00.00",
                                style = AppTypography.Body1.bold
                            )
                            Text(
                                text = "Waktu Tidur",
                                style = AppTypography.Subtitle2.regular,
                                color = GrayText
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "06.00",
                                style = AppTypography.Body1.bold
                            )
                            Text(
                                text = "Waktu Bangun",
                                style = AppTypography.Subtitle2.regular,
                                color = GrayText
                            )
                        }
                    }
                }
            }

            // --- SECTION: Riwayat Tidur (Fixed) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Riwayat Tidur", style = AppTypography.H5.bold)
                TextButton(
                    onClick = {
                        android.util.Log.d("SleepQuality", "Navigate to SleepHistory")
                        navController.navigate(Routes.SleepHistory)
                    }
                ) {
                    Text(text = "Lihat semua", style = AppTypography.Body1.medium)
                }
            }

            // --- CARD: Item Riwayat Tidur (Fixed) ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Kotak Tanggal (Hijau)
                    Column(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Primary500),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "18",
                            style = AppTypography.H4.bold,
                            color = Color.White
                        )
                        // FIXED: Mengganti Caption ke Subtitle2
                        Text(
                            text = "Agustus",
                            style = AppTypography.Subtitle2.regular, // Fallback dari Caption
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 2. Konten Teks & Tag
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Row untuk Judul & Tag "Normal"
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Total waktu tidur: 8 jam",
                                style = AppTypography.Body1.bold
                            )

                            // Tag/Chip "Normal"
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Primary100)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                // FIXED: Mengganti Caption ke Subtitle2 (atau gunakan Button jika ada)
                                Text(
                                    text = "Normal",
                                    style = AppTypography.Subtitle2.medium, // Fallback dari Caption
                                    color = Primary700
                                )
                            }
                        }

                        // Subtitle / Motivasi
                        // FIXED: Mengganti Body2 ke Body1
                        Text(
                            text = "Tidurmu makin teratur, proud of you!",
                            style = AppTypography.Body1.regular, // Fallback dari Body2
                            color = GrayText
                        )
                    }
                }
            }
        }
    }
}