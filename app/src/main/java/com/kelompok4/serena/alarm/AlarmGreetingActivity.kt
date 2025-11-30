package com.kelompok4.serena.alarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kelompok4.serena.ui.screens.GoodMorningScreen
import com.kelompok4.serena.ui.screens.GoodNightScreen
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AlarmGreetingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = intent.getStringExtra("TYPE") ?: "wake"
        val now = LocalDateTime.now()
        val timeString = now.format(DateTimeFormatter.ofPattern("HH.mm"))
        val date = now.toLocalDate()
        val userName = "Awa" // nanti bisa ambil dari profil / DataStore

        setContent {
            if (type == "sleep") {
                GoodNightScreen(
                    userName = userName,
                    timeString = timeString,
                    date = date,
                    onClose = { finish() }
                )
            } else {
                GoodMorningScreen(
                    userName = userName,
                    timeString = timeString,
                    date = date,
                    showSnooze = true,
                    onSnooze = {
                        // TODO: jadwalkan snooze baru kalau mau
                    },
                    onClose = { finish() }
                )
            }
        }
    }
}
