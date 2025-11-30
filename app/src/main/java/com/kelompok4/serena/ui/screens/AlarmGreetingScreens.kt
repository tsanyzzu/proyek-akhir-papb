package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kelompok4.serena.ui.theme.AppTypography
import com.kelompok4.serena.ui.theme.GrayText
import com.kelompok4.serena.ui.theme.Primary50
import com.kelompok4.serena.ui.theme.Primary500
import com.kelompok4.serena.ui.theme.Primary700
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Greeting screen saat jam tidur tiba.
 * Menampilkan salam, jam, dan tanggal, + tombol Tutup.
 */
@Composable
fun GoodNightScreen(
    userName: String,
    timeString: String,
    date: LocalDate,
    onClose: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary50)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Selamat Malam, $userName!",
            style = AppTypography.H4.bold,
            color = Primary700,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = timeString,
            style = AppTypography.H1.bold,
            color = Primary700
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = date.format(dateFormatter),
            style = AppTypography.Body1.regular,
            color = GrayText
        )

        Spacer(modifier = Modifier.height(32.dp))

        // TODO: taruh ilustrasi di sini kalau sudah punya drawable
        // Image(...)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary500)
        ) {
            Text(
                text = "Tutup",
                style = AppTypography.Button.medium,
                color = Color.White
            )
        }
    }
}

/**
 * Greeting screen saat alarm bangun bunyi.
 * Ada opsi Snooze (opsional) dan tombol Tutup.
 */
@Composable
fun GoodMorningScreen(
    userName: String,
    timeString: String,
    date: LocalDate,
    showSnooze: Boolean,
    onSnooze: () -> Unit = {},
    onClose: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary50)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Selamat Pagi, $userName!",
            style = AppTypography.H4.bold,
            color = Primary700,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = timeString,
            style = AppTypography.H1.bold,
            color = Primary700
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = date.format(dateFormatter),
            style = AppTypography.Body1.regular,
            color = GrayText
        )

        Spacer(modifier = Modifier.height(32.dp))

        // TODO: taruh ilustrasi di sini kalau sudah punya drawable
        // Image(...)

        Spacer(modifier = Modifier.weight(1f))

        if (showSnooze) {
            Button(
                onClick = onSnooze,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary50)
            ) {
                Text(
                    text = "Snooze",
                    style = AppTypography.Button.medium,
                    color = Primary500
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary500)
        ) {
            Text(
                text = "Tutup",
                style = AppTypography.Button.medium,
                color = Color.White
            )
        }
    }
}