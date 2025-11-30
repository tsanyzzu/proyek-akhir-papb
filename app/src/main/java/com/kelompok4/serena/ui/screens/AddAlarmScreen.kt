package com.kelompok4.serena.ui.screens

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kelompok4.serena.alarm.AlarmReceiver    // ganti sesuai package AlarmReceiver-mu
import com.kelompok4.serena.ui.theme.*
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

/**
 * Screen untuk membuat / mengedit alarm sehat.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlarmScreen(navController: NavHostController) {
    val context = LocalContext.current

    // State untuk jam tidur & bangun
    var sleepTime by remember { mutableStateOf(LocalTime.of(0, 0)) }
    var wakeTime by remember { mutableStateOf(LocalTime.of(6, 0)) }
    // Nama pengingat
    var reminderName by remember { mutableStateOf("") }
    // Snooze
    var snoozeEnabled by remember { mutableStateOf(false) }
    // Hari pengulangan
    val selectedDays = remember { mutableStateListOf<DayOfWeek>() }

    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH.mm") }

    // Hitung durasi (support lewat tengah malam)
    val totalMinutes = remember(sleepTime, wakeTime) {
        val diff = Duration.between(sleepTime, wakeTime).toMinutes()
        if (diff >= 0) diff else diff + Duration.ofHours(24).toMinutes()
    }
    val durationHours = totalMinutes / 60
    val durationMinutes = totalMinutes % 60

    Scaffold(
        containerColor = Primary50,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Alarm",
                        style = AppTypography.H6.bold,
                        color = Color.Black
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Primary50)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Durasi tidur
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format("%02d jam %02d menit", durationHours, durationMinutes),
                    style = AppTypography.H5.bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (durationHours < 8)
                        "Jadwal ini tidak memenuhi target tidur Anda"
                    else
                        "Jadwal ini memenuhi target tidur Anda",
                    style = AppTypography.Body1.regular,
                    color = GrayText
                )
            }

            // Kartu jam tidur & bangun
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Waktu tidur
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            TimePickerDialog(
                                context,
                                { _, hour: Int, minute: Int ->
                                    sleepTime = LocalTime.of(hour, minute)
                                },
                                sleepTime.hour,
                                sleepTime.minute,
                                true
                            ).show()
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Waktu Tidur",
                            style = AppTypography.Button.regular,
                            color = GrayText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = timeFormatter.format(sleepTime),
                            style = AppTypography.H4.bold,
                            color = Primary700
                        )
                    }
                }
                // Waktu bangun
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            TimePickerDialog(
                                context,
                                { _, hour: Int, minute: Int ->
                                    wakeTime = LocalTime.of(hour, minute)
                                },
                                wakeTime.hour,
                                wakeTime.minute,
                                true
                            ).show()
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Waktu Bangun",
                            style = AppTypography.Button.regular,
                            color = GrayText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = timeFormatter.format(wakeTime),
                            style = AppTypography.H4.bold,
                            color = Primary700
                        )
                    }
                }
            }

            // Hari pengulangan
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Ulangi Otomatis Setiap",
                    style = AppTypography.Button.regular,
                    color = GrayText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val days = listOf(
                        "S" to DayOfWeek.MONDAY,
                        "S" to DayOfWeek.TUESDAY,
                        "R" to DayOfWeek.WEDNESDAY,
                        "K" to DayOfWeek.THURSDAY,
                        "J" to DayOfWeek.FRIDAY,
                        "S" to DayOfWeek.SATURDAY,
                        "M" to DayOfWeek.SUNDAY
                    )
                    days.forEach { (label, day) ->
                        val isSelected = selectedDays.contains(day)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Primary500 else Primary100)
                                .clickable {
                                    if (isSelected) selectedDays.remove(day)
                                    else selectedDays.add(day)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = AppTypography.Button.medium,
                                color = if (isSelected) Color.White else Primary700
                            )
                        }
                    }
                }
            }

            // Nama pengingat
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Nama Pengingat",
                    style = AppTypography.Button.regular,
                    color = GrayText
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = reminderName,
                    onValueChange = { reminderName = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary500,
                        unfocusedBorderColor = Primary100,
                        focusedLabelColor = Primary500,
                        cursorColor = Primary500
                    )
                )
            }

            // Snooze
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ulangi Snooze",
                    style = AppTypography.Button.regular,
                    color = GrayText
                )
                Switch(
                    checked = snoozeEnabled,
                    onCheckedChange = { snoozeEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Primary500,
                        checkedThumbColor = Color.White,
                        uncheckedTrackColor = Primary100,
                        uncheckedThumbColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tombol aksi
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(width = 1.dp, color = Primary500)
                ) {
                    Text("Hapus", style = AppTypography.Button.medium, color = Primary500)
                }
                Button(
                    onClick = {
                        // 1. Jadwalkan alarm seperti sebelumnya
                        scheduleAlarm(
                            context = context,
                            hour = sleepTime.hour,
                            minute = sleepTime.minute,
                            requestCode = 1000,
                            type = "sleep"
                        )
                        scheduleAlarm(
                            context = context,
                            hour = wakeTime.hour,
                            minute = wakeTime.minute,
                            requestCode = 1001,
                            type = "wake"
                        )

                        // 2. Format jam jadi string, misal "23.30"
                        val sleepStr = timeFormatter.format(sleepTime)
                        val wakeStr  = timeFormatter.format(wakeTime)

                        // 3. Kirim ke screen sebelumnya (SleepQualityScreen)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("sleepTime", sleepStr)

                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("wakeTime", wakeStr)

                        // 4. Kembali ke SleepQualityScreen
                        navController.navigateUp()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary500)
                ) {
                    Text("Simpan", style = AppTypography.Button.medium, color = Color.White)
                }

            }
        }
    }
}

/**
 * Fungsi helper untuk menjadwalkan alarm dengan AlarmManager.
 */
fun scheduleAlarm(
    context: Context,
    hour: Int,
    minute: Int,
    requestCode: Int,
    type: String // "sleep" atau "wake"
) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("TYPE", type)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val cal = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        if (before(Calendar.getInstance())) {
            // Kalau jam sudah lewat, jadwalkan besok
            add(Calendar.DATE, 1)
        }
    }

    // PENTING: jangan pakai setExactAndAllowWhileIdle di sini dulu
    alarmManager.set(
        AlarmManager.RTC_WAKEUP,
        cal.timeInMillis,
        pendingIntent
    )
}
