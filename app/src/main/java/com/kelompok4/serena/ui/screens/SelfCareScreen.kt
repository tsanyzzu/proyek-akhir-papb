package com.example.serena.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.serena.ui.components.ActivityCard
import com.example.serena.ui.components.ArticleCard
import com.example.serena.ui.components.RecommendationCard
import com.example.serena.ui.components.SectionHeader
import com.kelompok4.serena.ui.theme.*
import com.kelompok4.serena.R
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelfCareScreen(
    navController: NavHostController? = null
) {
    val searchQuery = remember { mutableStateOf("") }

    /**
     * Sample data for articles and activities.  Replace these lists
     * with real data from your ViewModel or repository when
     * integrating into your app.
     */
    val articles = listOf(
        SampleArticle(
            id = 1,
            title = "Manfaat meditasi pagi untuk kesehatan mental",
            thumbnail = R.drawable.onboarding_1,
            description = "Temukan cara sederhana untuk memulai hari dengan lebih tenang melalui meditasi pagi."
        ),
        SampleArticle(
            id = 2,
            title = "Teknik Pernapasan untuk Mengurangi Stres",
            thumbnail = R.drawable.onboarding_1,
            description = "Pelajari cara mengatur napas untuk menenangkan pikiran dan meningkatkan fokus."
        )
    )
    val activities = listOf(
        SampleActivity(
            id = 1,
            title = "Latihan Pernapasan untuk Mengurangi Stres",
            thumbnail = R.drawable.onboarding_1,
            date = "February 27, 2025",
            views = 700,
            likes = 512
        ),
        SampleActivity(
            id = 2,
            title = "Rutinitas Meditasi Pagi",
            thumbnail = R.drawable.onboarding_1,
            date = "February 01, 2025",
            views = 450,
            likes = 321
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
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
            // Recommendation Card
            RecommendationCard(
                painterRes = R.drawable.onboarding_1,
                title = "Serena punya rekomendasi buat kamu!",
                message = "Kondisi mental kamu sedang dalam keadaan baik. Jaga kesehatanmu dengan rekomendasi artikel dan kegiatan dari Serena.",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Artikel Section
            SectionHeader(
                title = "Artikel",
                onClickSeeAll = { navController?.navigate("articles") }
            )
            LazyRow(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
            ) {
                items(articles) { article ->
                    ArticleCard(
                        painterRes = article.thumbnail,
                        title = article.title,
                        isVertical = false,
                        onClick = {
                            navController?.navigate("articleDetail/${article.id}")
                        }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Activities Section
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
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = {
                    navController?.navigate("activityDetail/${activity.id}")
                }
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
        // Kita tidak perlu mengirim navController karena default-nya sudah null
        SelfCareScreen()
    }
}