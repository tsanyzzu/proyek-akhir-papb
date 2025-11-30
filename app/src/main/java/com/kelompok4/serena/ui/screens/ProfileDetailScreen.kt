package com.kelompok4.serena.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kelompok4.serena.R
import com.kelompok4.serena.ui.theme.AppTypography
import com.kelompok4.serena.ui.theme.Primary500
import com.kelompok4.serena.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileDetailScreen(
    navController: NavController,
    userEmail: String,
    vm: ProfileViewModel = viewModel() // pastikan shared ViewModel scope via navGraph jika ingin benar-benar shared
) {
    val context = LocalContext.current
    val user by vm.user.collectAsState()

    // local state untuk dialog edit email
    var showEditEmailDialog by remember { mutableStateOf(false) }
    var newEmailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") } // untuk reauth jika perlu
    var isUpdatingEmail by remember { mutableStateOf(false) }

    LaunchedEffect(userEmail) {
        vm.loadUserByEmail(userEmail)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))

        // Avatar
        if (!user?.profilePhotoUrl.isNullOrBlank()) {
            AsyncImage(
                model = user?.profilePhotoUrl,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clickable { /* ganti foto */ }
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.default_profile),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clickable { /* ganti foto */ }
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(text = "Ganti Foto", style = AppTypography.Body1.medium, color = Primary500)
        Spacer(Modifier.height(20.dp))

        // Username card
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                ProfileFieldRow(
                    label = "Username",
                    value = user?.username ?: "-",
                    onEdit = { navController.navigate("edit_value/$userEmail/username") }
                )
            }
        }

        // Fullname card
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                ProfileFieldRow(
                    label = "Nama Lengkap",
                    value = user?.fullName ?: "-",
                    onEdit = { navController.navigate("edit_value/$userEmail/fullname") }
                )
            }
        }

        // Email card (BISA DIUBAH)
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                ProfileFieldRow(
                    label = "Email",
                    value = user?.email ?: "-",
                    onEdit = {
                        // open dialog untuk edit email
                        newEmailInput = user?.email ?: ""
                        passwordInput = ""
                        showEditEmailDialog = true
                    }
                )
            }
        }
    }

    // Dialog Edit Email
    if (showEditEmailDialog) {
        AlertDialog(
            onDismissRequest = { if (!isUpdatingEmail) showEditEmailDialog = false },
            title = { Text("Ubah Email") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newEmailInput,
                        onValueChange = { newEmailInput = it },
                        label = { Text("Email baru") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Kata sandi (jika diminta)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Jika Anda mendapat pesan bahwa sesi Anda sudah lama, masukkan kata sandi untuk verifikasi ulang. Jika tidak ingat, silakan login ulang.",
                        style = AppTypography.Subtitle2.regular,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !isUpdatingEmail,
                    onClick = {
                        val newEmail = newEmailInput.trim()
                        if (newEmail.isBlank()) {
                            Toast.makeText(context, "Email baru tidak boleh kosong", Toast.LENGTH_SHORT).show()
                            return@TextButton
                        }
                        isUpdatingEmail = true
                        vm.updateEmail(newEmail, passwordInput.ifBlank { null }) { success, message ->
                            isUpdatingEmail = false
                            if (success) {
                                Toast.makeText(context, "Email berhasil diperbarui", Toast.LENGTH_SHORT).show()
                                showEditEmailDialog = false
                            } else {
                                Toast.makeText(context, message ?: "Gagal mengubah email", Toast.LENGTH_LONG).show()
                                // jika message menyebut perlu login ulang, kamu bisa arahkan user untuk logout/login
                            }
                        }
                    }
                ) {
                    if (isUpdatingEmail) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Simpan")
                    }
                }
            },
            dismissButton = {
                TextButton(enabled = !isUpdatingEmail, onClick = { showEditEmailDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

/** Reusable row UI */
@Composable
private fun ProfileFieldRow(label: String, value: String, onEdit: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = AppTypography.Subtitle2.medium)
            Spacer(Modifier.height(4.dp))
            Text(text = value, style = AppTypography.Body1.medium)
        }
        Text(
            text = "UBAH",
            style = AppTypography.Body1.medium,
            color = Primary500,
            modifier = Modifier.clickable { onEdit() }
        )
    }
}
