package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.foundation.border
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kelompok4.serena.R
import com.kelompok4.serena.ui.theme.*

// Enum untuk membedakan tipe konten
enum class ContentType { ARTIKEL, KEGIATAN }

// Data class dummy yang lebih sesuai dengan desain
data class SelfCareContent(
    val id: Int,
    val title: String,
    val category: String,
    val source: String, // bisa nama channel video atau penulis artikel
    val imageRes: Int, // Menggunakan resource drawable untuk dummy
    val type: ContentType
)

// List data dummy
val dummyContents = listOf(
    SelfCareContent(1, "Cara Meditasi untuk Pemula", "Meditasi", "Calm", R.drawable.onboarding_1, ContentType.KEGIATAN),
    SelfCareContent(2, "Pentingnya Menjaga Kesehatan Mental", "Kesehatan", "Satu Persen", R.drawable.onboarding_2, ContentType.ARTIKEL),
    SelfCareContent(3, "Musik Relaksasi untuk Tidur Nyenyak", "Musik", "Sleepify", R.drawable.onboarding_3, ContentType.KEGIATAN),
    SelfCareContent(4, "Manfaat Menulis Jurnal Harian", "Jurnal", "Riliv", R.drawable.onboarding_1, ContentType.ARTIKEL),
    SelfCareContent(5, "Latihan Pernapasan untuk Meredakan Cemas", "Pernapasan", "Headspace", R.drawable.onboarding_2, ContentType.KEGIATAN),
    SelfCareContent(6, "Mengenal Tanda-tanda Burnout", "Psikologi", "KlikDokter", R.drawable.onboarding_3, ContentType.ARTIKEL)
)

@Composable
fun SelfCareScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Artikel", "Kegiatan")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp) // Padding atas untuk keseluruhan layar
    ) {
        // --- Judul Halaman & Search Bar dalam Padding Horizontal ---
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Self Care",
                style = AppTypography.H3.bold, // Menggunakan AppTypography
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            SearchBar()
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- Tabs ---
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = Primary500,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 3.dp,
                    color = Primary500
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = AppTypography.Body1.medium // Menggunakan AppTypography
                        )
                    },
                    selectedContentColor = Primary500,
                    unselectedContentColor = DisabledTextGray // Menggunakan warna dari design system
                )
            }
        }

        // --- Konten Berdasarkan Tab ---
        val contentToShow = when (tabs[selectedTabIndex]) {
            "Artikel" -> dummyContents.filter { it.type == ContentType.ARTIKEL }
            "Kegiatan" -> dummyContents.filter { it.type == ContentType.KEGIATAN }
            else -> emptyList()
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(contentToShow) { content ->
                ContentCard(content = content)
            }
        }
    }
}

@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }

    // Menggunakan BasicTextField untuk styling kustom yang presisi
    BasicTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = AppTypography.Subtitle2.regular.copy(color = Color.Black), // Gaya untuk teks input
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = DisabledGray, // Sesuai permintaan: 0xFFE4E4E7
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .padding(start = 16.dp, top = 10.dp, end = 16.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = DisabledTextGray, // Sesuai permintaan: 0xFF71717A
                    modifier = Modifier.size(24.dp) // Sesuai permintaan: width/height 24.dp
                )

                // Box untuk menampung placeholder dan input field
                Box(contentAlignment = Alignment.CenterStart) {
                    // Tampilkan placeholder jika input kosong
                    if (text.isEmpty()) {
                        Text(
                            text = "Cari", // Teks placeholder sesuai permintaan
                            style = AppTypography.Subtitle2.regular,
                            color = DisabledTextGray
                        )
                    }
                    // Ini adalah tempat input teks akan muncul
                    innerTextField()
                }
            }
        }
    )
}

@Composable
fun ContentCard(content: SelfCareContent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Primary50) // Border halus
    ) {
        Column {
            Image(
                painter = painterResource(id = content.imageRes),
                contentDescription = content.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = content.title,
                        style = AppTypography.H6.bold, // Menggunakan AppTypography
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    // Kategori sebagai tag kecil
                    Box(
                        modifier = Modifier
                            .background(color = Primary50, shape = CircleShape)
                            .clip(CircleShape)
                    ) {
                        Text(
                            text = content.category,
                            color = Primary500,
                            style = AppTypography.Button.medium, // Menggunakan AppTypography
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Oleh: ${content.source}",
                    style = AppTypography.Subtitle2.regular, // Menggunakan AppTypography
                    color = GrayText // Menggunakan warna dari design system
                )
            }
        }
    }
}