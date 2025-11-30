package com.example.serena.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.serena.ui.screens.SampleArticle
import com.kelompok4.serena.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    navController: NavHostController,
    articleId: Int
) {
    // In a real app you would load the article by ID from a repository.
    val article = SampleArticle(
        id = articleId,
        title = "Manfaat Meditasi Pagi untuk Kesehatan Mental",
        thumbnail = R.drawable.serena_logo,
        description = "Meditasi pagi telah lama dikenal sebagai salah satu cara efektif untuk meningkatkan kesehatan mental dan mempersiapkan diri menghadapi hari dengan lebih tenang dan fokus.\n\n" +
                "1. Mengurangi Stres dan Kecemasan\nSaat bangun tidur, pikiran kita sering kali dipenuhi dengan berbagai rencana dan kekhawatiran tentang hari yang akan datang. Meditasi pagi membantu menenangkan pikiran dan mengurangi stres dengan memperlambat detak jantung serta menurunkan kadar hormon kortisol yang berperan dalam respons stres.\n\n" +
                "2. Meningkatkan Konsentrasi dan Fokus\nMeditasi pagi melatih otak untuk lebih fokus dan waspada sepanjang hari. Dengan mengatur pernapasan dan memusatkan perhatian pada saat ini, meditasi membantu mengurangi distraksi dan meningkatkan produktivitas dalam aktivitas sehari-hari.\n\n" +
                "3. Meningkatkan Kualitas Tidur\nMeditasi pagi juga dapat berkontribusi pada kualitas tidur yang lebih baik. Dengan menenangkan pikiran dan tubuh di pagi hari, siklus tidur dapat menjadi lebih teratur dan mendalam."
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: handle bookmark */ }) {
                        Icon(imageVector = Icons.Outlined.Bookmark, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(id = article.thumbnail),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = article.title,
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Text(
                    text = article.description,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}