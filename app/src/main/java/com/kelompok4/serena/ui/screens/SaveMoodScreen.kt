package com.kelompok4.serena.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kelompok4.serena.data.Mood
import com.kelompok4.serena.data.MoodDataManager
import com.kelompok4.serena.data.MoodTypes
import com.kelompok4.serena.ui.components.AppButton
import com.kelompok4.serena.ui.components.ButtonType
import com.kelompok4.serena.ui.theme.*

@Composable
fun SaveMoodScreen(
    navController: NavHostController,
    userEmail: String
) {
    val context = LocalContext.current

    // PERBAIKAN UX: Inisialisasi dengan GEMBIRA agar tombol tidak mati di awal
    // dan status visual sinkron dengan status data
    var selectedMood by remember { mutableStateOf<String?>(MoodTypes.GEMBIRA) }

    val moodOptions = listOf(
        MoodTypes.GEMBIRA to "😊",
        MoodTypes.SEDIH to "😢",
        MoodTypes.NETRAL to "😐",
        MoodTypes.MARAH to "😠",
        MoodTypes.DEPRESI to "😭"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Secondary100),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Bagaimana perasaanmu hari ini?",
            style = AppTypography.H4.bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Main Mood Display Card
        Card(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Secondary300),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tampilan Emoji (Logic disederhanakan karena selectedMood tidak lagi null di awal)
                Text(
                    text = moodOptions.find { it.first == selectedMood }?.second ?: "😊",
                    fontSize = 120.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Mood Name
                Text(
                    text = selectedMood ?: "Gembira",
                    style = AppTypography.H3.bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Mood Description
                Text(
                    text = when (selectedMood) {
                        MoodTypes.GEMBIRA -> "Mood kamu lagi oke banget! Yuk lakukan hal-hal yang kamu suka!"
                        MoodTypes.SEDIH -> "Gapapa merasa sedih, itu wajar kok. Yuk cerita ke Serena!"
                        MoodTypes.NETRAL -> "Hari yang biasa aja ya? Semoga besok lebih baik!"
                        MoodTypes.MARAH -> "Tarik nafas dulu yuk. Serena siap dengerin kamu."
                        MoodTypes.DEPRESI -> "Kamu nggak sendirian. Serena ada buat kamu."
                        else -> "Pilih mood kamu hari ini."
                    },
                    style = AppTypography.Subtitle2.medium,
                    color = Primary700,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Mood Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            moodOptions.forEach { (moodName, emoji) ->
                MoodSelectorButton(
                    emoji = emoji,
                    isSelected = selectedMood == moodName,
                    onClick = { selectedMood = moodName }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save Button
        AppButton(
            text = "Simpan Mood",
            onClick = {
                selectedMood?.let { mood ->
                    // Membuat objek Mood baru (otomatis date = System.currentTimeMillis)
                    val newMood = Mood(
                        moodName = mood,
                        moodEmoji = MoodTypes.getMoodEmoji(mood),
                        userEmail = userEmail
                    )

                    val success = MoodDataManager.addMood(context, newMood)
                    if (success) {
                        Toast.makeText(context, "Mood berhasil disimpan!", Toast.LENGTH_SHORT).show()

                        // Navigasi ke Recap, hapus screen ini dari backstack
                        navController.navigate("mood_recap/$userEmail") {
                            popUpTo("save_mood/$userEmail") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Gagal menyimpan mood", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            buttonType = ButtonType.PRIMARY,
            // Tombol selalu aktif karena defaultnya sudah terpilih
            enabled = selectedMood != null
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun MoodSelectorButton(
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(if (isSelected) Primary500 else Color.White)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) Primary700 else DisabledGray,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 32.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SaveMoodScreenPreview() {
    ProyekakhirpapbTheme {
        SaveMoodScreen(
            navController = rememberNavController(),
            userEmail = "contoh@email.com"
        )
    }
}