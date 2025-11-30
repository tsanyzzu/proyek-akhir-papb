package com.kelompok4.serena.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kelompok4.serena.ui.components.AppButton
import com.kelompok4.serena.ui.components.ButtonType
import com.kelompok4.serena.ui.theme.AppTypography
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.kelompok4.serena.ui.viewmodel.ProfileViewModel

@Composable
fun EditValueScreen(
    navController: NavController,
    userEmail: String,
    field: String,
    profileVmParam: ProfileViewModel? = null
) {
    val context = LocalContext.current

    // Try to reuse parent's ViewModel (so it's the same instance as Profile screens)
    val parentEntry = navController.previousBackStackEntry
    val vm: ProfileViewModel = profileVmParam ?: if (parentEntry != null) {
        viewModel(parentEntry)
    } else {
        viewModel()
    }

    val userState by vm.user.collectAsState()
    val user = userState

    // Ensure the ViewModel loads the user
    LaunchedEffect(userEmail) {
        vm.loadUserByEmail(userEmail)
    }

    // initial value depends on field
    val initialValue = when (field) {
        "username" -> user?.username ?: ""
        "fullname" -> user?.fullName ?: ""
        "email" -> user?.email ?: ""
        else -> ""
    }

    var input by remember { mutableStateOf(initialValue) }
    // keep input synced when user loaded
    LaunchedEffect(user) {
        if (input.isEmpty()) input = when (field) {
            "username" -> user?.username ?: ""
            "fullname" -> user?.fullName ?: ""
            "email" -> user?.email ?: ""
            else -> ""
        }
    }

    // For email re-auth (optional)
    var passwordInput by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = when (field) {
                "username" -> "Ubah username"
                "fullname" -> "Ubah nama lengkap"
                "email" -> "Ubah email"
                else -> "Ubah nilai"
            },
            style = AppTypography.H5.bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = {
                Text(
                    text = when (field) {
                        "username" -> "Username"
                        "fullname" -> "Nama Lengkap"
                        else -> "Email"
                    }
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = if (field == "email") KeyboardOptions(keyboardType = KeyboardType.Email) else KeyboardOptions.Default,
            visualTransformation = if (field == "email") VisualTransformation.None else VisualTransformation.None
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Jika edit email, sediakan optional password untuk reauth
        if (field == "email") {
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("Kata sandi (jika diminta)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Jika sesi Anda sudah lama, Firebase mungkin meminta verifikasi ulang — masukkan kata sandi untuk verifikasi.",
                style = AppTypography.Subtitle2.regular
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        val canSave = input.isNotBlank() && input != initialValue && !isSaving

        AppButton(
            text = if (isSaving) "Menyimpan..." else "Simpan perubahan",
            onClick = {
                // disable double click
                if (!canSave) return@AppButton

                isSaving = true

                when (field) {
                    "username" -> {
                        vm.updateUsername(input) { success, message ->
                            isSaving = false
                            if (success) {
                                Toast.makeText(context, "Username berhasil diubah", Toast.LENGTH_SHORT).show()
                                navController.navigate("success_profile/$userEmail") {
                                    // optional: clear/optimize backstack (ke start destination)
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }

                            } else {
                                Toast.makeText(context, message ?: "Gagal mengubah username", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    "fullname" -> {
                        vm.updateFullName(input) { success, message ->
                            isSaving = false
                            if (success) {
                                Toast.makeText(context, "Nama berhasil diubah", Toast.LENGTH_SHORT).show()
                                navController.navigate("success_profile/$userEmail") {
                                    // optional: clear/optimize backstack (ke start destination)
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }

                            } else {
                                Toast.makeText(context, message ?: "Gagal mengubah nama", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    "email" -> {
                        // call updateEmail which handles reauth if needed
                        vm.updateEmail(input, passwordInput.ifBlank { null }) { success, message ->
                            isSaving = false
                            if (success) {
                                Toast.makeText(context, "Email berhasil diubah", Toast.LENGTH_SHORT).show()
                                navController.navigate("success_profile/$userEmail") {
                                    // optional: clear/optimize backstack (ke start destination)
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            } else {
                                // jika butuh reauth dan password kosong, message akan menjelaskan
                                Toast.makeText(context, message ?: "Gagal mengubah email", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    else -> {
                        isSaving = false
                        Toast.makeText(context, "Field tidak dikenali", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.PRIMARY,
            enabled = canSave
        )
    }
}