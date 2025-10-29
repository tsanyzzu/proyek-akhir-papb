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
import com.kelompok4.serena.ui.navigation.Routes
import com.kelompok4.serena.ui.theme.AppTypography
import com.kelompok4.serena.ui.viewmodel.EditValueViewModel

@Composable
fun EditValueScreen(
    navController: NavController,
    userEmail: String,
    field: String,
    vm: EditValueViewModel = viewModel()
) {
    val context = LocalContext.current
    val user by vm.user.collectAsState()

    LaunchedEffect(Unit) { vm.load(context, userEmail) }

    var input by remember { mutableStateOf("") }
    val initial = when (field) {
        "username" -> user?.username ?: ""
        "fullname" -> user?.fullName ?: ""
        "email" -> user?.email ?: ""
        else -> ""
    }
    LaunchedEffect(user) { if (input.isEmpty()) input = initial }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(Modifier.height(8.dp))
        Text(text = "Ubah ${when(field){ "username"->"username"; "fullname"->"nama lengkap"; else->"email"}}", style = AppTypography.H5.bold)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text(text = when(field){ "username"->"Username"; "fullname"->"Nama Lengkap"; else->"Email"}) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        val canSave = input.isNotBlank() && input != initial

        AppButton(
            text = "Simpan perubahan",
            onClick = {
                val u = user ?: return@AppButton
                val updated = when (field) {
                    "username" -> u.copy(username = input)
                    "fullname" -> u.copy(fullName = input)
                    "email" -> u.copy(email = input)
                    else -> u
                }
                vm.save(context, updated) {
                    navController.navigate("success_profile") {
                        popUpTo(Routes.MAIN) // bersihkan stack jika perlu
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.PRIMARY,
            enabled = canSave
        )
    }
}