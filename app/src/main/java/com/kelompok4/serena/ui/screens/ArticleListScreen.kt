package com.example.serena.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.serena.ui.components.ArticleCard
import com.example.serena.ui.screens.SampleArticle
import com.kelompok4.serena.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleListScreen(navController: NavHostController) {
    val categories = listOf("Terbaru", "Kecemasan", "Stres", "Sosial", "Kesehatan")
    val selectedCategory = remember { mutableStateOf(categories.first()) }
    val searchQuery = remember { mutableStateOf("") }

    // This uses the same sample articles as the SelfCareScreen for demonstration.
    val articles = listOf(
        SampleArticle(
            id = 1,
            title = "Manfaat meditasi pagi untuk kesehatan mental",
            thumbnail = R.drawable.serena_logo,
            description = "Temukan cara sederhana untuk memulai hari dengan lebih tenang melalui meditasi pagi."
        ),
        SampleArticle(
            id = 2,
            title = "Teknik Pernapasan untuk Mengurangi Stres",
            thumbnail = R.drawable.serena_logo,
            description = "Pelajari cara mengatur napas untuk menenangkan pikiran dan meningkatkan fokus."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Artikel") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text(text = "Cari") },
                leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
                singleLine = true
            )
            Spacer(modifier = Modifier.padding(8.dp))
            // Category chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    AssistChip(
                        onClick = { selectedCategory.value = category },
                        label = { Text(text = category) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (selectedCategory.value == category) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent,
                            labelColor = if (selectedCategory.value == category) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            // Article list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(articles) { article ->
                    ArticleCard(
                        painterRes = article.thumbnail,
                        title = article.title,
                        subtitle = article.description,
                        isVertical = true,
                        onClick = {
                            navController.navigate("articleDetail/${article.id}")
                        }
                    )
                }
            }
        }
    }
}