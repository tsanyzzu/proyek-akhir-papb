package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kelompok4.serena.ui.theme.*

// Model data sederhana untuk list
data class SleepHistoryItem(
    val date: String,
    val month: String,
    val hours: Int,
    val status: String, // "Normal", "Kurang", "Lebih"
    val message: String,
    val isHealthy: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepHistoryScreen(navController: NavHostController) {
    // Dummy Data sesuai gambar referensi
    val historyList = listOf(
        SleepHistoryItem("18", "Agustus", 8, "Normal", "Tidurmu makin teratur, proud of you!", true),
        SleepHistoryItem("17", "Agustus", 5, "Kurang", "Waktu tidurmu kurang, ayo perbaiki!", false),
        SleepHistoryItem("16", "Agustus", 7, "Normal", "Pertahankan pola tidur sehat ini!", true),
        SleepHistoryItem("15", "Agustus", 4, "Insomnia", "Jangan terlalu banyak begadang ya.", false)
    )

    Scaffold(
        containerColor = Primary50, // Background hijau muda
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Riwayat Tidur",
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
                    // Spacer untuk menyeimbangkan judul di tengah
                    Spacer(modifier = Modifier.width(48.dp))
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
                .padding(horizontal = 16.dp)
        ) {
            // --- Filter Bulan (Dropdown style) ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Agustus",
                    style = AppTypography.H5.bold,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Pilih Bulan",
                    tint = Color.Black
                )
            }

            // --- List Riwayat ---
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(historyList) { item ->
                    SleepHistoryCard(item)
                }
            }
        }
    }
}

@Composable
fun SleepHistoryCard(item: SleepHistoryItem) {
    // Menentukan warna berdasarkan status kesehatan tidur
    val primaryColor = if (item.isHealthy) Primary500 else Color(0xFFFF6B6B) // Hijau atau Merah
    val lightBackgroundColor = if (item.isHealthy) Primary100 else Color(0xFFFFEBEE) // Hijau muda atau Merah muda
    val textColor = if (item.isHealthy) Primary700 else Color(0xFFC62828)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Kotak Tanggal
            Column(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(primaryColor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.date,
                    style = AppTypography.H4.bold,
                    color = Color.White
                )
                Text(
                    text = item.month,
                    style = AppTypography.Subtitle2.regular, // Ganti Caption jika error
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Detail Teks
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Total waktu tidur: ${item.hours} jam",
                        style = AppTypography.Body1.bold
                    )

                    // Chip Status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(lightBackgroundColor)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = item.status,
                            style = AppTypography.Subtitle2.medium,
                            color = textColor
                        )
                    }
                }

                Text(
                    text = item.message,
                    style = AppTypography.Body1.regular,
                    color = GrayText
                )
            }
        }
    }
}