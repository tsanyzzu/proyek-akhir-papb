package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Pastikan import ini ada
import androidx.navigation.NavHostController
import com.kelompok4.serena.data.JournalDataManager
import com.kelompok4.serena.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalDetailScreen(
    navController: NavHostController,
    journalId: String
) {
    val context = LocalContext.current
    val journal = remember { JournalDataManager.getJournalById(context, journalId) }

    if (journal == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Jurnal tidak ditemukan",
                style = AppTypography.Body1.regular,
                color = GrayText
            )
        }
        return
    }

    val dateStr = remember(journal.date) {
        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy\nHH:mm", Locale("id", "ID"))
        sdf.format(Date(journal.date))
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Jurnal",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Mood and Date Section
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
                            text = journal.moodEmoji,
                            style = AppTypography.H4.regular,
                            // PERBAIKAN 1: Pastikan ini menggunakan .sp langsung
                            fontSize = 36.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = journal.mood,
                            style = AppTypography.H6.bold,
                            color = Primary700
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dateStr,
                            style = AppTypography.Subtitle2.regular,
                            color = GrayText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = journal.title,
                style = AppTypography.H5.bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Primary50.copy(alpha = 0.3f))
            ) {
                Text(
                    text = journal.content,
                    style = AppTypography.Body1.regular,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp),
                    // PERBAIKAN 2: Ubah pemanggilan fungsi menjadi extension property
                    lineHeight = 24.sp
                )
            }
        }
    }
}