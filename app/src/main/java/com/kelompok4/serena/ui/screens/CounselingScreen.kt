package com.kelompok4.serena.ui.screens

import androidx.compose. foundation.Image
import androidx.compose.foundation. background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy. items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material. icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose. runtime.*
import androidx.compose. ui.Alignment
import androidx. compose.ui.Modifier
import androidx.compose.ui.draw. clip
import androidx.compose.ui.graphics.Color
import androidx. compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui. text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose. ui.unit.sp
import androidx.navigation.NavHostController
import com.kelompok4.serena.R
import com.kelompok4.serena.data.CounselingDataManager
import com.kelompok4.serena.ui.components.CounselingScheduleCard
import com. kelompok4.serena. ui.theme.*

@Composable
fun CounselingScreen(navController: NavHostController, userEmail: String) {
    val context = LocalContext.current

    // State untuk menyimpan jadwal konseling
    var scheduledCounseling by remember { mutableStateOf(listOf<com.kelompok4.serena.data.Counseling>()) }

    // Load data konseling
    LaunchedEffect(Unit) {
        scheduledCounseling = CounselingDataManager.getScheduledCounseling(context, userEmail)
    }

    Scaffold(
        topBar = { TopSearchBar() },
        containerColor = BaseColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                // Tagline Banner
                TaglineBanner()
                Spacer(modifier = Modifier. height(16.dp))
            }

            // Section Jadwal Konsultasi (hanya tampil jika ada jadwal)
            if (scheduledCounseling.isNotEmpty()) {
                item {
                    Text(
                        text = "Jadwal Konsultasi",
                        style = AppTypography. H6.bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(scheduledCounseling) { counseling ->
                    CounselingScheduleCard(
                        counseling = counseling,
                        onClick = {
                            // Navigate ke detail konseling
                            // navController.navigate("counseling_detail/${counseling.id}")
                        },
                        modifier = Modifier. padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier. height(16.dp))
                }
            }

            // Counselor Recommendations
            item {
                CounselorSection(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar() {
    var searchText by remember { mutableStateOf("") }

    TopAppBar(
        title = {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                placeholder = {
                    Text(
                        text = "Cari",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp),
                        tint = Color. Gray
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Primary500
                ),
                singleLine = true
            )
        },
        actions = {
            IconButton(
                onClick = { /* TODO: Navigasi ke History */ },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    tint = Primary500,
                    modifier = Modifier. size(28.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun TaglineBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary500
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16. dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier. weight(1f)
            ) {
                Text(
                    text = "#BersamaUntukSemua",
                    style = AppTypography.H6.bold,
                    color = Color. White
                )
                Spacer(modifier = Modifier. height(8.dp))
                Text(
                    text = "\"Kesehatan mental adalah hak setiap individu.  Dengan dukungan yang tepat, kita bisa tumbuh lebih kuat bersama\"",
                    style = AppTypography. Subtitle2.regular,
                    color = Color. White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun CounselorSection(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16. dp)
    ) {
        Row(
            modifier = Modifier. fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rekomendasi Konselor",
                style = AppTypography.H6.bold,
                color = Color.Black
            )
            TextButton(onClick = { /* TODO: Lihat semua */ }) {
                Text(
                    text = "Lihat semua",
                    style = AppTypography.Subtitle2.medium,
                    color = Primary500
                )
            }
        }

        // TODO: Add counselor list here
    }
}

