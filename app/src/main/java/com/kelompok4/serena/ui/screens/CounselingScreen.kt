package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale // Import Added
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kelompok4.serena.R

@Composable
fun CounselingScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(2) }

    Scaffold(
        topBar = { TopSearchBar() },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            // tagline Banner
            TaglineBanner()

            Spacer(modifier = Modifier.height(16.dp))

            // Counselor Recommendations
            CounselorSection()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar() {
    // State untuk teks pencarian
    var searchText by remember { mutableStateOf("") }

    TopAppBar(
        title = {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it }, // 'it' sekarang akan dikenali sebagai String
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp), // Tinggi 50dp
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
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color(0xFF2D7D5F)
                ),
                singleLine = true
                // HAPUS baris contentPadding karena tidak didukung oleh OutlinedTextField
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
                    tint = Color(0xFF2D7D5F),
                    modifier = Modifier.size(28.dp)
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
            containerColor = Color(0xFF2D7D5F)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "#BersamaUntukSemua",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"Kesehatan mental adalah hak setiap individu. Dengan dukungan yang tepat, kita bisa tumbuh lebih kuat bersama\"",
                    color = Color.White,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            // Placeholder for doctor image
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

@Composable
fun CounselorSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rekomendasi Konselor",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = {}) {
                Text(
                    text = "Lihat semua",
                    color = Color(0xFF2D7D5F)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Kartu Pertama (konselor1)
            CounselorCard(
                name = "Dr. Laura Azzura, S.Psi.",
                specialty = "Psikolog Klinis",
                price = "Rp 150.000",
                isFree = false,
                imageRes = R.drawable.konselor1 // Gambar 1
            )

            // Kartu Kedua (konselor2)
            CounselorCard(
                name = "Dr. Sarah Putri",
                specialty = "Psikiater",
                price = "Rp 200.000",
                isFree = false,
                imageRes = R.drawable.konselor2 // Gambar 2
            )
        }
    }
}

@Composable
fun CounselorCard(
    name: String,
    specialty: String,
    price: String,
    isFree: Boolean,
    imageRes: Int // Tambahkan parameter ini
) {
    Card(
        modifier = Modifier
            .width(600.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar Dokter Dinamis
            Image(
                painter = painterResource(id = imageRes), // Gunakan parameter imageRes di sini
                contentDescription = "Foto Ahli",
                contentScale = ContentScale.Crop, // Agar gambar mengisi kotak dengan rapi
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = specialty,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isFree) Color.Black else Color(0xFF2D7D5F)
                )
            }

            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF2D7D5F)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF2D7D5F))
                )
            ) {
                Text("Reservasi", fontSize = 12.sp)
            }
        }
    }
}

@Preview
@Composable
fun CounselingScreenPreview() {
    CounselingScreen(navController = NavHostController(LocalContext.current))
}