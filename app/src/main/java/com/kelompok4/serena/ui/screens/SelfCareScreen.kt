package com.example.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Untuk ukuran font emoji
import androidx.navigation.NavHostController
import com.example.serena.ui.components.ActivityCard
import com.example.serena.ui.components.ArticleCard
import com.example.serena.ui.components.SectionHeader
import com.kelompok4.serena.R
import com.kelompok4.serena.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelfCareScreen(
    navController: NavHostController? = null
) {
    val searchQuery = remember { mutableStateOf("") }

    // ... (Data SampleArticle dan SampleActivity tetap sama) ...
    val articles = listOf(
        SampleArticle(1, "Manfaat meditasi pagi...", R.drawable.onboarding_1, "Deskripsi..."),
        SampleArticle(2, "Teknik Pernapasan...", R.drawable.onboarding_1, "Deskripsi...")
    )
    val activities = listOf(
        SampleActivity(1, "Latihan Pernapasan...", R.drawable.onboarding_1, "Feb 27", 700, 512),
        SampleActivity(2, "Rutinitas Meditasi...", R.drawable.onboarding_1, "Feb 01", 450, 321)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            // Search bar
            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                placeholder = { Text(text = "Cari") },
                leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- REKOMENDASI CARD YANG BARU ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Primary500) // Warna background ungu/biru tua
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bagian Kiri: Teks
                    Column(
                        modifier = Modifier.weight(1f) // Mengambil sisa ruang agar teks tidak tertimpa icon
                    ) {
                        Text(
                            text = "Serena punya rekomendasi buat kamu!",
                            style = AppTypography.Body1.bold, // Menggunakan style Bold
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Kondisi mental kamu sedang dalam keadaan baik. Jaga kesehatanmu dengan rekomendasi artikel dan kegiatan dari Serena.",
                            style = AppTypography.Subtitle2.regular, // Style regular lebih kecil
                            color = Color.White.copy(alpha = 0.9f) // Sedikit transparan agar tidak terlalu kontras
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp)) // Jarak antara teks dan icon

                    // Bagian Kanan: Icon Senyum (Emoji)
                    Box(
                        modifier = Modifier
                            .size(60.dp) // Ukuran lingkaran latar belakang
                            .clip(CircleShape)
                            .background(Color.White), // Latar belakang putih
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "😊", // Icon senyum (Emoji)
                            fontSize = 32.sp // Ukuran font emoji
                        )
                    }
                }
            }
            // -------------------------------------

            Spacer(modifier = Modifier.height(24.dp))

            // ... (Sisa kode Artikel dan Kegiatan tetap sama) ...
            SectionHeader(
                title = "Artikel",
                onClickSeeAll = { navController?.navigate("articles") }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(articles) { article ->
                    ArticleCard(
                        painterRes = article.thumbnail,
                        title = article.title,
                        isVertical = false,
                        onClick = { navController?.navigate("articleDetail/${article.id}") }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(
                title = "Kegiatan",
                onClickSeeAll = { navController?.navigate("activities") }
            )
        }
        items(activities) { activity ->
            ActivityCard(
                painterRes = activity.thumbnail,
                title = activity.title,
                date = activity.date,
                views = activity.views,
                likes = activity.likes,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = { navController?.navigate("activityDetail/${activity.id}") }
            )
        }
    }
}

// Data classes for sample data.  These should be replaced with your actual
// models when integrating into your project.
data class SampleArticle(
    val id: Int,
    val title: String,
    val thumbnail: Int,
    val description: String
)

data class SampleActivity(
    val id: Int,
    val title: String,
    val thumbnail: Int,
    val date: String,
    val views: Int,
    val likes: Int
)

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SelfCareScreenPreview() {
    ProyekakhirpapbTheme {
        SelfCareScreen()
    }
}