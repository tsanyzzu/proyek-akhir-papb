package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.kelompok4.serena.R
import com.kelompok4.serena.data.*
import com.kelompok4.serena.ui.navigation.Routes
import com.kelompok4.serena.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(navController: NavController, userEmail: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollState = rememberScrollState()

    // Ambil nama user (tetap pakai produceState karena jarang berubah)
    val fullNameState by produceState<String?>(initialValue = null, key1 = userEmail) {
        try {
            val user = UserDataManager.getUserByEmail(userEmail)
            value = user?.fullName
        } catch (e: Exception) {
            e.printStackTrace()
            value = null
        }
    }

    var currentMood by remember { mutableStateOf<Mood?>(null) }

    LaunchedEffect(Unit) {
        try {
            currentMood = MoodDataManager.getLatestMood(context, userEmail)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    // Lifecycle Observer: Akan jalan setiap kali layar Home 'RESUME' (muncul kembali)
    // --- PERBAIKAN 2: Load ulang saat KEMBALI ke halaman ini (Resume) ---
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                try {
                    currentMood = MoodDataManager.getLatestMood(context, userEmail)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    // ------------------------------------------------

    // Logika UI (Sama seperti sebelumnya)
    if (currentMood != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary50)
                .verticalScroll(scrollState)
        ) {
            HeaderSection(
                navController = navController,
                userEmail = userEmail,
                currentMood = currentMood,
                fullName = fullNameState
            )
            HomeContent(navController = navController, userEmail = userEmail)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary50)
        ) {
            HeaderSection(
                navController = navController,
                userEmail = userEmail,
                currentMood = null,
                fullName = fullNameState
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                HomeContent(navController = navController, userEmail = userEmail)
            }
        }
    }
}

// Komponen Konten Body (TIDAK BERUBAH)
@Composable
fun HomeContent(navController: NavController, userEmail: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        OneOnOneCard(navController = navController)

        Spacer(modifier = Modifier.height(16.dp))
        SerenaScoreCard()
        Spacer(modifier = Modifier.height(24.dp))
        JournalSection(navController = navController, userEmail = userEmail)
        Spacer(modifier = Modifier.height(24.dp))
        SleepQualitySection(navController)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// HeaderSection (DIPERBARUI)
@Composable
fun HeaderSection(
    navController: NavController,
    userEmail: String,
    currentMood: Mood?,
    fullName: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Primary50)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val profilePhotoUrl by produceState<String?>(initialValue = null, key1 = userEmail) {
                try {
                    val u = UserDataManager.getUserByEmail(userEmail)
                    value = u?.profilePhotoUrl
                } catch (e: Exception) {
                    e.printStackTrace()
                    value = null
                }
            }

            if (!profilePhotoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = profilePhotoUrl,
                    contentDescription = "Foto Profil",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                        .clickable { navController.navigate("profile/$userEmail") },
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.default_profile),
                    error = painterResource(id = R.drawable.default_profile)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Foto Profil",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { navController.navigate("profile/$userEmail") },
                    tint = Primary500
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Column {
                val displayName = when {
                    !fullName.isNullOrBlank() -> fullName
                    userEmail.isNotBlank() -> userEmail.substringBefore("@")
                    else -> "User"
                }

                Text(
                    text = "Halo, $displayName",
                    style = AppTypography.Body1.regular
                )
            }
            Spacer(modifier = Modifier.weight(1.0f))
            IconButton(
                onClick = { /* TODO: Aksi notifikasi */ },
                modifier = Modifier
                    .size(40.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
                    .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifikasi",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentMood != null) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mood Terakhir Kamu",
                        style = AppTypography.H4.bold
                    )
                    TextButton(
                        onClick = {
                            navController.navigate("save_mood/$userEmail")
                        }
                    ) {
                        Text(
                            text = "Ganti",
                            style = AppTypography.Subtitle2.medium,
                            color = Primary500
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .clickable {
                            navController.navigate("mood_recap/$userEmail")
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentMood.moodName,
                                style = AppTypography.H4.bold,
                                color = Primary700
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = getMoodDescription(currentMood.moodName),
                                style = AppTypography.Subtitle2.regular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = currentMood.moodEmoji,
                            fontSize = 56.sp
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selamat Pagi!",
                    style = AppTypography.H2.bold
                )
                Text(
                    text = "Bagaimana perasaanmu hari ini?",
                    style = AppTypography.Subtitle2.regular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                MoodIcon(icon = Icons.Default.SentimentVerySatisfied, navController = navController, userEmail = userEmail)
                MoodIcon(icon = Icons.Default.SentimentSatisfied, navController = navController, userEmail = userEmail)
                MoodIcon(icon = Icons.Default.SentimentNeutral, navController = navController, userEmail = userEmail)
                MoodIcon(icon = Icons.Default.SentimentDissatisfied, navController = navController, userEmail = userEmail)
                MoodIcon(icon = Icons.Default.SentimentVeryDissatisfied, navController = navController, userEmail = userEmail)
            }
        }
    }
}

// MoodIcon (TIDAK BERUBAH)
@Composable
fun MoodIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    navController: NavController,
    userEmail: String
) {
    IconButton(
        onClick = {
            navController.navigate("save_mood/$userEmail")
        },
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Primary500)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(36.dp),
            tint = Color.White
        )
    }
}

fun getMoodDescription(moodName: String): String {
    return when (moodName) {
        MoodTypes.GEMBIRA -> "Energi positifmu menular! Manfaatkan hari ini untuk hal produktif."
        MoodTypes.SEDIH -> "Tidak apa-apa merasa sedih. Ambil waktu sejenak untuk dirimu sendiri."
        MoodTypes.NETRAL -> "Hari yang tenang. Jalani dengan santai dan tetap fokus."
        MoodTypes.MARAH -> "Tarik napas dalam-dalam. Tenangkan pikiran sebelum bertindak."
        MoodTypes.DEPRESI -> "Kamu tidak sendirian. Jangan ragu mencari dukungan jika perlu."
        else -> "Tetap semangat menjalani hari ini!"
    }
}

// OneOnOneCard (TIDAK BERUBAH)
@Composable
fun OneOnOneCard(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.0f)) {
                Text(
                    text = "1-on-1 Dengan Ahli",
                    style = AppTypography.H4.bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Konsultasi mudah dengan ahli terpercaya. Pilih, jadwalkan, mulai!",
                    style = AppTypography.Subtitle2.regular,
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = {
                    navController.navigate(Routes.KONSELING) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }) {
                    Text(text = "Pesan Sekarang", style = AppTypography.Button.bold, color = Primary700)
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Primary700, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(id = R.drawable.tanteseksi),
                contentDescription = "Foto Ahli",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}

// SerenaScoreCard (TIDAK BERUBAH)
@Composable
fun SerenaScoreCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Skor Serena",
                style = AppTypography.Body1.bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Primary50)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Skor Serena",
                            modifier = Modifier.fillMaxSize(),
                            tint = Primary500
                        )
                        Text(
                            text = "80",
                            style = AppTypography.H2.bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Sehat & Stabil",
                            style = AppTypography.Body1.bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Kondisi mental kamu lagi oke! Jaga energi positif ini...",
                            style = AppTypography.Subtitle2.regular,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

// JournalSection (TIDAK BERUBAH - hanya auto-refresh mood, jurnal perlu logic serupa jika mau auto-refresh)
@Composable
fun JournalSection(navController: NavController, userEmail: String) {
    val context = LocalContext.current
    var latestJournal by remember { mutableStateOf<com.kelompok4.serena.data.Journal?>(null) }

    // Logic refresh jurnal (Opsional: bisa ditambahkan LifecycleObserver juga jika perlu)
    LaunchedEffect(Unit) {
        latestJournal = JournalDataManager.getLatestJournal(context, userEmail)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            title = "Jurnal Pribadi",
            onSeeAllClick = {
                navController.navigate("journal_list/$userEmail")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (latestJournal != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Primary500),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = latestJournal!!.moodEmoji,
                                style = AppTypography.Body1.regular,
                                fontSize = 20.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = latestJournal!!.mood, style = AppTypography.Body1.bold)
                            val dateStr = remember(latestJournal!!.date) {
                                val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                                sdf.format(Date(latestJournal!!.date))
                            }
                            Text(text = dateStr, style = AppTypography.Button.regular, color = GrayText)
                        }
                        Spacer(modifier = Modifier.weight(1.0f))
                        TextButton(onClick = {
                            navController.navigate("add_journal/$userEmail/${latestJournal!!.id}")
                        }) {
                            Text("Edit", style = AppTypography.Subtitle2.medium, color = Primary500)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = latestJournal!!.title, style = AppTypography.Subtitle2.bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (latestJournal!!.content.length > 100)
                            "${latestJournal!!.content.take(100)}..."
                        else
                            latestJournal!!.content,
                        style = AppTypography.Button.regular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Primary50),
            onClick = {
                navController.navigate("add_journal/$userEmail")
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Tambah Jurnal",
                    tint = Primary500
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Tambahkan jurnal dan catat perasaan...",
                    style = AppTypography.Subtitle2.medium
                )
            }
        }
    }
}

// SleepQualitySection (TIDAK BERUBAH)
@Composable
fun SleepQualitySection(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = "Kualitas Tidur", onSeeAllClick = { /*TODO*/ })
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            onClick = { navController.navigate(Routes.SleepQuality) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1.0f)) {
                    Text(
                        text = "Kualitas Tidur",
                        style = AppTypography.Body1.bold
                    )
                    Text(
                        text = "Selama 7 hari terakhir",
                        style = AppTypography.Button.regular
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .background(Primary50, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Kualitas Tidur Baik",
                            color = Primary500,
                            style = AppTypography.Button.bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Primary500,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier.size(70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = 0.8f,
                        modifier = Modifier.fillMaxSize(),
                        color = Primary500,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 6.dp
                    )
                    Text(
                        text = "80",
                        style = AppTypography.H4.bold
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = AppTypography.H6.bold
        )
        TextButton(onClick = onSeeAllClick) {
            Text(text = "Lihat semua", style = AppTypography.Subtitle2.medium, color = Primary500)
        }
    }
}
@Preview(showBackground = true, widthDp = 360, heightDp = 1200)
@Composable
fun HomeScreenPreview() {
    ProyekakhirpapbTheme {
        HomeScreen(navController = rememberNavController(), userEmail = "user@example.com")
    }
}