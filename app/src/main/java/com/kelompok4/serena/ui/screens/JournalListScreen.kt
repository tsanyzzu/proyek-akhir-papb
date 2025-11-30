package com.kelompok4.serena.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Import ini wajib ada
import androidx.navigation.NavHostController
import com.kelompok4.serena.data.Journal
import com.kelompok4.serena.data.JournalDataManager
import com.kelompok4.serena.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalListScreen(
    navController: NavHostController,
    userEmail: String
) {
    val context = LocalContext.current
    var journals by remember { mutableStateOf<List<Journal>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var journalToDelete by remember { mutableStateOf<Journal?>(null) }

    // Load journals (refresh saat layar dibuka)
    LaunchedEffect(Unit) {
        journals = JournalDataManager.getJournalsByUser(context, userEmail)
    }

    Scaffold(
        containerColor = Primary50,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Jurnal Pribadi",
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add_journal/$userEmail")
                },
                containerColor = Primary500,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Tambah Jurnal"
                )
            }
        }
    ) { paddingValues ->
        if (journals.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "📝",
                    style = AppTypography.H1.regular,
                    // PERBAIKAN 1: Gunakan .sp extension
                    fontSize = 72.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Belum Ada Jurnal",
                    style = AppTypography.H5.bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Mulai catat perasaan dan pengalamanmu hari ini",
                    style = AppTypography.Body1.regular,
                    color = GrayText,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(journals) { journal ->
                    JournalCard(
                        journal = journal,
                        onClick = {
                            navController.navigate("journal_detail/${journal.id}")
                        },
                        onEdit = {
                            navController.navigate("add_journal/$userEmail/${journal.id}")
                        },
                        onDelete = {
                            journalToDelete = journal
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog && journalToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(text = "Hapus Jurnal?", style = AppTypography.H6.bold)
            },
            text = {
                Text(
                    text = "Apakah kamu yakin ingin menghapus jurnal \"${journalToDelete!!.title}\"?",
                    style = AppTypography.Body1.regular
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val success = JournalDataManager.deleteJournal(context, journalToDelete!!.id)
                        if (success) {
                            journals = JournalDataManager.getJournalsByUser(context, userEmail)
                            Toast.makeText(context, "Jurnal berhasil dihapus", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Gagal menghapus jurnal", Toast.LENGTH_SHORT).show()
                        }
                        showDeleteDialog = false
                        journalToDelete = null
                    }
                ) {
                    Text("Hapus", color = TertiaryRed500)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal", color = Primary500)
                }
            }
        )
    }
}

@Composable
fun JournalCard(
    journal: Journal,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val dateStr = remember(journal.date) {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        sdf.format(Date(journal.date))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mood Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Primary500),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = journal.moodEmoji,
                        style = AppTypography.H6.regular,
                        // PERBAIKAN 2: Gunakan .sp extension
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = journal.mood,
                        style = AppTypography.Body1.bold
                    )
                    Text(
                        text = dateStr,
                        style = AppTypography.Button.regular,
                        color = GrayText
                    )
                }

                // Menu button
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = GrayText
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                showMenu = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Hapus", color = TertiaryRed500) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = journal.title,
                style = AppTypography.Subtitle2.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Content preview
            Text(
                text = journal.content,
                style = AppTypography.Button.regular,
                color = GrayText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}