package com.kelompok4.serena.ui.screens

import android.widget.Toast
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kelompok4.serena.data.Journal
import com.kelompok4.serena.data.JournalDataManager
import com.kelompok4.serena.data.MoodOptions
import com.kelompok4.serena.ui.components.AppButton
import com.kelompok4.serena.ui.components.ButtonType
import com.kelompok4.serena.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJournalScreen(
    navController: NavHostController,
    userEmail: String,
    journalId: String? = null // Untuk mode edit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Load existing journal jika mode edit
    LaunchedEffect(journalId) {
        journalId?.let {
            val existingJournal = JournalDataManager.getJournalById(context, it)
            existingJournal?.let { journal ->
                title = journal.title
                content = journal.content
                selectedMood = journal.mood
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (journalId == null) "Tambah Jurnal" else "Edit Jurnal",
                        style = AppTypography.H6.bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
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
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Judul Jurnal
            Text(
                text = "Judul Jurnal",
                style = AppTypography.Body1.bold
            )
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Tuliskan judul jurnal di sini...") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary500,
                    unfocusedBorderColor = DisabledGray
                )
            )

            // Mood Kamu
            Text(
                text = "Mood Kamu",
                style = AppTypography.Body1.bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MoodOptions.moods.forEach { mood ->
                    MoodButton(
                        emoji = mood.emoji,
                        label = mood.name,
                        isSelected = selectedMood == mood.name,
                        onClick = { selectedMood = mood.name }
                    )
                }
            }

            // Isi Jurnal
            Text(
                text = "Tuliskan Isi Jurnal",
                style = AppTypography.Body1.bold
            )
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                placeholder = { Text("Catatan: Hari ini aku mendapat hadiah dari pasangan. Sederhana, tapi sangat berarti.....") },
                shape = RoundedCornerShape(12.dp),
                maxLines = 10,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary500,
                    unfocusedBorderColor = DisabledGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Simpan
            AppButton(
                text = "Simpan Jurnal",
                onClick = {
                    when {
                        title.isBlank() -> {
                            Toast.makeText(context, "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        }
                        selectedMood == null -> {
                            Toast.makeText(context, "Pilih mood terlebih dahulu", Toast.LENGTH_SHORT).show()
                        }
                        content.isBlank() -> {
                            Toast.makeText(context, "Isi jurnal tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            isLoading = true
                            val moodOption = MoodOptions.getMoodByName(selectedMood!!)

                            val journal = if (journalId != null) {
                                // Mode edit - preserve ID
                                Journal(
                                    id = journalId,
                                    title = title,
                                    content = content,
                                    mood = selectedMood!!,
                                    moodEmoji = moodOption?.emoji ?: "😊",
                                    date = System.currentTimeMillis(),
                                    userEmail = userEmail
                                )
                            } else {
                                // Mode add - new ID
                                Journal(
                                    title = title,
                                    content = content,
                                    mood = selectedMood!!,
                                    moodEmoji = moodOption?.emoji ?: "😊",
                                    userEmail = userEmail
                                )
                            }

                            val success = if (journalId != null) {
                                JournalDataManager.updateJournal(context, journal)
                            } else {
                                JournalDataManager.addJournal(context, journal)
                            }

                            isLoading = false
                            if (success) {
                                Toast.makeText(
                                    context,
                                    if (journalId != null) "Jurnal berhasil diupdate!" else "Jurnal berhasil disimpan!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigateUp()
                            } else {
                                Toast.makeText(context, "Gagal menyimpan jurnal", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                buttonType = ButtonType.PRIMARY,
                enabled = !isLoading && title.isNotBlank() && selectedMood != null && content.isNotBlank()
            )
        }
    }
}

@Composable
fun MoodButton(
    emoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(if (isSelected) Primary500 else Primary50)
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) Primary700 else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                style = AppTypography.H4.regular,
// Benar
                fontSize = 28.sp            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = AppTypography.Button.medium,
            color = if (isSelected) Primary700 else GrayText
        )
    }
}