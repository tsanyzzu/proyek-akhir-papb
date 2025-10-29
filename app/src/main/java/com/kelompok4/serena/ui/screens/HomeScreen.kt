package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Import dari theme Anda
import com.kelompok4.serena.ui.theme.AppTypography
import com.kelompok4.serena.ui.theme.Primary300
import com.kelompok4.serena.ui.theme.Primary50
import com.kelompok4.serena.ui.theme.Primary500
import com.kelompok4.serena.ui.theme.Primary700
import com.kelompok4.serena.ui.theme.ProyekakhirpapbTheme
// import com.kelompok4.serena.R // Import R Anda untuk drawable (jika ada)


@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            // --- PERUBAHAN 1 ---
            // Latar belakang utama sekarang adalah hijau muda (Primary50)
            // bukan lagi MaterialTheme.colorScheme.background (White)
            .background(Primary50)
    ) {
        // BAGIAN 1: HEADER (TETAP)
        // HeaderSection sudah memiliki background Primary50,
        // jadi akan menyatu dengan sempurna.
        HeaderSection()

        // BAGIAN 2: KONTEN (BISA SCROLL)
        Column(
            modifier = Modifier
                .fillMaxSize()
                // --- PERUBAHAN 2 ---
                // 1. Terapkan kliping sudut tumpul HANYA di bagian atas
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                // 2. Beri latar belakang putih PADA kolom ini
                .background(MaterialTheme.colorScheme.background)
                // --- Akhir Perubahan ---
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp) // Padding untuk konten di dalam area putih
        ) {
            // Spacer ini sekarang berada di dalam area putih,
            // memberi jarak dari tepi atas yang tumpul
            Spacer(modifier = Modifier.height(24.dp))

            OneOnOneCard()
            Spacer(modifier = Modifier.height(16.dp))
            SerenaScoreCard()
            Spacer(modifier = Modifier.height(24.dp))
            JournalSection()
            Spacer(modifier = Modifier.height(24.dp))
            SleepQualitySection()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- BAGIAN HEADER (TETAP) ---

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Primary50) // Latar belakang hijau muda dari theme
            .padding(16.dp)
        // JANGAN tambahkan horizontalAlignment di sini,
        // agar Row di atasnya tetap rata kiri-kanan
    ) {
        // Baris: Foto Profil, Nama, Lonceng
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto Profil",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                tint = Primary500
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Halo, Ahsana Iklila",
                    style = AppTypography.Body1.regular
                )
            }
            Spacer(modifier = Modifier.weight(1.0f))
            IconButton(
                onClick = { /* TODO: Aksi notifikasi */ },
                modifier = Modifier
                    .size(40.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(12.dp)
                    )
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

        // --- PERUBAHAN DIMULAI DI SINI ---

        // 1. Bungkus kedua Text dalam Column baru
        Column(
            modifier = Modifier.fillMaxWidth(), // 2. Buat Column ini selebar layar
            horizontalAlignment = Alignment.CenterHorizontally // 3. Ratakan tengah semua isinya
        ) {
            Text(
                text = "Selamat Pagi!",
                style = AppTypography.H2.bold // Menggunakan AppTypography
            )
            Text(
                text = "Bagaimana perasaanmu hari ini?",
                style = AppTypography.Subtitle2.regular, // Menggunakan AppTypography
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            MoodIcon(icon = Icons.Default.SentimentVerySatisfied)
            MoodIcon(icon = Icons.Default.SentimentSatisfied)
            MoodIcon(icon = Icons.Default.SentimentNeutral)
            MoodIcon(icon = Icons.Default.SentimentDissatisfied)
            MoodIcon(icon = Icons.Default.SentimentVeryDissatisfied)
        }
    }
}

@Composable
fun MoodIcon(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    IconButton(
        onClick = { /* TODO: Aksi pilih mood */ },
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Primary500)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(36.dp), // Ukuran ikon di dalam lingkaran
            tint = Color.White // Warna ikon menjadi putih
        )
    }
}


@Composable
fun OneOnOneCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        // Kartu ini "putih" (sesuai background utama), bukan Primary50 (surface)
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Beri sedikit bayangan
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.0f)) {
                Text(
                    text = "1-on-1 Dengan Ahli",
                    style = AppTypography.H4.bold // Menggunakan AppTypography
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Konsultasi mudah dengan ahli terpercaya. Pilih, jadwalkan, mulai!",
                    style = AppTypography.Subtitle2.regular,


                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Pesan Sekarang", style = AppTypography.Button.bold, color = Primary700)
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Primary700, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.Person, // Placeholder
                contentDescription = "Foto Ahli",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                tint = Primary500 // Hijau tua dari theme
            )
        }
    }
}

@Composable
fun SerenaScoreCard() {
    // --- PERUBAHAN 1 ---
    // Ini adalah Card PUTIH luar yang baru.
    // Kodenya diambil dari Card lain (seperti JournalSection)
    // agar seragam (putih + shadow).
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // --- PERUBAHAN 2 ---
        // Tambahkan Column untuk menampung Judul dan Card Hijau
        Column(
            modifier = Modifier.padding(16.dp) // Beri padding untuk isi card putih
        ) {
            // --- PERUBAHAN 3 ---
            // Tambahkan judul "Skor Serena"
            Text(
                text = "Skor Serena",
                style = AppTypography.Body1.bold, // Gunakan style yang sama dengan header lain
                modifier = Modifier.padding(bottom = 8.dp) // Beri jarak ke card hijau
            )

            // --- PERUBAHAN 4 ---
            // Ini adalah Card HIJAU (Primary50) yang ASLI dari kode Anda.
            // Sekarang dia berada di dalam Card putih.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Primary50) // Kartu ini hijau muda
            ) {
                // Isi card hijau (Row, Box, Icon, Teks) tetap sama
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
                            tint = Primary500 // Hijau tua dari theme
                        )
                        Text(
                            text = "80",
                            style = AppTypography.H2.bold, // Menggunakan AppTypography
                            color = Color.White // Teks putih di atas ikon
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Sehat & Stabil",
                            style = AppTypography.Body1.bold // Menggunakan AppTypography
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Kondisi mental kamu lagi oke! Jaga energi positif ini...",
                            style = AppTypography.Subtitle2.regular, // Menggunakan AppTypography
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JournalSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = "Jurnal Pribadi", onSeeAllClick = { /*TODO*/ })
        Spacer(modifier = Modifier.height(8.dp))

        // Kartu Jurnal Terakhir (Putih)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // --- PERUBAHAN DI SINI ---
                    // Ganti Icon tunggal dengan Box + Icon
                    Box(
                        modifier = Modifier
                            .size(32.dp) // Ukuran lingkaran luar (sesuai kode asli Anda)
                            .clip(CircleShape)
                            .background(Primary500), // Latar belakang lingkaran hijau
                        contentAlignment = Alignment.Center // Pusatkan ikon di dalam Box
                    ) {
                        Icon(
                            imageVector = Icons.Default.SentimentSatisfied,
                            contentDescription = "Mood Gembira",
                            tint = Color.White, // Ikonnya menjadi putih
                            modifier = Modifier.size(20.dp) // Ukuran ikon di dalam (lebih kecil)
                        )
                    }
                    // --- AKHIR PERUBAHAN ---

                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "Gembira", style = AppTypography.Body1.bold)
                        Text(text = "Kemarin", style = AppTypography.Button.regular)
                    }
                    Spacer(modifier = Modifier.weight(1.0f))
                    TextButton(onClick = { /*TODO*/ }) {
                        Text("Edit", style = AppTypography.Subtitle2.medium, color = Primary500)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Kamu merasa Gembira", style = AppTypography.Subtitle2.regular)
                Text(text = "Karena Pasangan anda", style = AppTypography.Subtitle2.regular )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Catatan: Hari ini aku mendapat hadiah dari pasangan. Sederhana, tapi sangat berarti.....",
                    style = AppTypography.Button.regular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Kartu Tambah Jurnal (Hijau Muda)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Primary50),
            onClick = { /*TODO: Aksi tambah jurnal*/ }
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
                    tint = MaterialTheme.colorScheme.primary // Hijau tua
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Tambahkan jurnal dan catat perasaan...",
                    style = AppTypography.Subtitle2.medium // Menggunakan medium
                )
            }
        }
    }
}

@Composable
fun SleepQualitySection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = "Kualitas Tidur", onSeeAllClick = { /*TODO*/ })
        Spacer(modifier = Modifier.height(8.dp))

        // Kartu Kualitas Tidur (Putih)
        Card(
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
                            .background(Primary50, RoundedCornerShape(8.dp)) // Latar hijau muda
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Kualitas Tidur Baik",
                            color = MaterialTheme.colorScheme.primary, // Teks hijau tua
                            style = AppTypography.Button.bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary, // Ikon hijau tua
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
                        color = MaterialTheme.colorScheme.primary, // Hijau tua
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


// --- KOMPONEN BANTU ---

@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = AppTypography.H6.bold // Menggunakan AppTypography
        )
        TextButton(onClick = onSeeAllClick) {
            Text(text = "Lihat semua", style = AppTypography.Subtitle2.medium, color = Primary500)
        }
    }
}


// --- PREVIEW ---

@Preview(showBackground = true, widthDp = 360, heightDp = 1200)
@Composable
fun HomeScreenPreview() {
    // Bungkus dengan Tema proyek Anda
    ProyekakhirpapbTheme {
        HomeScreen()
    }
}