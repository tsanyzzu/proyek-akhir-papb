package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kelompok4.serena.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlarmScreen(navController: NavHostController) {
    var sleepHour by remember { mutableStateOf(22) }
    var sleepMinute by remember { mutableStateOf(0) }
    var wakeHour by remember { mutableStateOf(6) }
    var wakeMinute by remember { mutableStateOf(0) }
    
    val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
    var selectedDays by remember { mutableStateOf(setOf("Sen", "Sel", "Rab", "Kam", "Jum")) }
    
    var showSleepTimePicker by remember { mutableStateOf(false) }
    var showWakeTimePicker by remember { mutableStateOf(false) }
    
    val sleepTimePickerState = rememberTimePickerState(
        initialHour = sleepHour,
        initialMinute = sleepMinute,
        is24Hour = true
    )
    
    val wakeTimePickerState = rememberTimePickerState(
        initialHour = wakeHour,
        initialMinute = wakeMinute,
        is24Hour = true
    )

    Scaffold(
        containerColor = Primary50,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tambah Alarm",
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
                    containerColor = Primary50
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Header Card with Alarm Icon
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Primary100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Alarm,
                            contentDescription = null,
                            tint = Primary500,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Atur Jadwal Tidur Sehat",
                        style = AppTypography.H5.bold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Tidur 7-9 jam untuk kesehatan optimal",
                        style = AppTypography.Body1.regular,
                        color = GrayText,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Sleep Time Card
            TimePickerCard(
                title = "Waktu Tidur",
                icon = Icons.Filled.Bedtime,
                hour = sleepHour,
                minute = sleepMinute,
                onClick = { showSleepTimePicker = true }
            )
            
            // Wake Time Card
            TimePickerCard(
                title = "Waktu Bangun",
                icon = Icons.Filled.WbSunny,
                hour = wakeHour,
                minute = wakeMinute,
                onClick = { showWakeTimePicker = true }
            )
            
            // Day Selection Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Ulangi",
                        style = AppTypography.H6.bold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        days.forEach { day ->
                            val isSelected = selectedDays.contains(day)
                            DayChip(
                                day = day,
                                isSelected = isSelected,
                                onClick = {
                                    selectedDays = if (isSelected) {
                                        selectedDays - day
                                    } else {
                                        selectedDays + day
                                    }
                                }
                            )
                        }
                    }
                }
            }
            
            // Sleep Duration Info
            val sleepDuration = calculateSleepDuration(sleepHour, sleepMinute, wakeHour, wakeMinute)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (sleepDuration >= 7) Primary100 else TertiaryRed100
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Alarm,
                        contentDescription = null,
                        tint = if (sleepDuration >= 7) Primary500 else TertiaryRed500,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = "Durasi Tidur: ${sleepDuration.toInt()} jam ${((sleepDuration % 1) * 60).toInt()} menit",
                            style = AppTypography.Body1.bold,
                            color = if (sleepDuration >= 7) Primary700 else TertiaryRed700
                        )
                        Text(
                            text = if (sleepDuration >= 7) "Durasi tidur sehat!" else "Kurang dari 7 jam, tambah waktu tidur",
                            style = AppTypography.Subtitle2.regular,
                            color = if (sleepDuration >= 7) Primary500 else TertiaryRed500
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Save Button
            Button(
                onClick = {
                    // TODO: Save alarm to database
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary500
                )
            ) {
                Text(
                    text = "Simpan Alarm",
                    style = AppTypography.Body1.bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
    
    // Sleep Time Picker Dialog
    if (showSleepTimePicker) {
        TimePickerDialog(
            onDismiss = { showSleepTimePicker = false },
            onConfirm = {
                sleepHour = sleepTimePickerState.hour
                sleepMinute = sleepTimePickerState.minute
                showSleepTimePicker = false
            }
        ) {
            TimePicker(state = sleepTimePickerState)
        }
    }
    
    // Wake Time Picker Dialog
    if (showWakeTimePicker) {
        TimePickerDialog(
            onDismiss = { showWakeTimePicker = false },
            onConfirm = {
                wakeHour = wakeTimePickerState.hour
                wakeMinute = wakeTimePickerState.minute
                showWakeTimePicker = false
            }
        ) {
            TimePicker(state = wakeTimePickerState)
        }
    }
}

@Composable
private fun TimePickerCard(
    title: String,
    icon: ImageVector,
    hour: Int,
    minute: Int,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Primary50),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Primary500,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = AppTypography.Subtitle2.regular,
                    color = GrayText
                )
                Text(
                    text = String.format("%02d:%02d", hour, minute),
                    style = AppTypography.H4.bold,
                    color = Color.Black
                )
            }
            
            Text(
                text = "Ubah",
                style = AppTypography.Body1.medium,
                color = Primary500
            )
        }
    }
}

@Composable
private fun DayChip(
    day: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isSelected) Primary500 else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (isSelected) Primary500 else GrayText,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.first().toString(),
            style = AppTypography.Subtitle2.medium,
            color = if (isSelected) Color.White else GrayText
        )
    }
}

@Composable
private fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

private fun calculateSleepDuration(
    sleepHour: Int,
    sleepMinute: Int,
    wakeHour: Int,
    wakeMinute: Int
): Float {
    val sleepMinutes = sleepHour * 60 + sleepMinute
    val wakeMinutes = wakeHour * 60 + wakeMinute
    
    val durationMinutes = if (wakeMinutes >= sleepMinutes) {
        wakeMinutes - sleepMinutes
    } else {
        (24 * 60 - sleepMinutes) + wakeMinutes
    }
    
    return durationMinutes / 60f
}
