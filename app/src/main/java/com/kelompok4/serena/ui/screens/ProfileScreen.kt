package com.kelompok4.serena.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kelompok4.serena.R
import androidx.compose.material3.Button
import com.kelompok4.serena.ui.theme.*
import com.kelompok4.serena.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    userEmail: String,
    vm: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val user by vm.user.collectAsState()

    LaunchedEffect(userEmail) { vm.loadUser(context, userEmail) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseColor)
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        // Header profile
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.default_profile),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .border(1.dp, Color.Black, CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                val usernameFromEmail = user?.email?.substringBefore("@") ?: "-"
                Text(text = usernameFromEmail, style = AppTypography.H6.bold)
                Spacer(Modifier.height(4.dp))
                Text(text = user?.email ?: "-", style = AppTypography.Subtitle2.regular, color = GrayText)
            }
        }

        Spacer(Modifier.height(20.dp))

        // Pengaturan akun
        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .background(Color.White)
                .padding(12.dp)) {
                Text("Pengaturan Akun", style = AppTypography.Subtitle2.bold)
                Spacer(Modifier.height(10.dp))

                SettingRow(
                    label = "Keamanan Akun",
                    subtitle = "Kata sandi, PIN",
                    iconRes = R.drawable.ic_lock
                ) { /* Navigasi ke keamanan */ }

                SettingRow(
                    label = "Edit Profil",
                    subtitle = "Atur informasi pribadi",
                    iconRes = R.drawable.ic_profile_card
                ) {
                    navController.navigate("profile_detail/${userEmail}")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Pengaturan umum
        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Text("Pengaturan Umum", style = AppTypography.Subtitle2.bold)
                Spacer(Modifier.height(10.dp))

                GeneralSettingRow("FAQ", "Cari pertanyaan anda", R.drawable.ic_help)
                GeneralSettingRow("Bahasa", "Pilih bahasa sesuai preferensi", R.drawable.ic_language)
                GeneralSettingRow("Tema", "Atur tampilan sesuai kenyamanan", R.drawable.ic_moon)
                GeneralSettingRow("Tentang Kami", "Informasi mengenai aplikasi", R.drawable.ic_info)
            }
        }

        Spacer(Modifier.weight(1f))

        // Tombol keluar
        Button(
            onClick = { /* Logout flow */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Keluar", style = AppTypography.Body1.medium)
        }

        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun SettingRow(label: String, subtitle: String, iconRes: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = AppTypography.Body1.medium)
            Text(subtitle, style = AppTypography.Subtitle2.regular, color = GrayText)
        }
    }
}

@Composable
private fun GeneralSettingRow(title: String, subtitle: String, iconRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(text = title, style = AppTypography.Body1.medium)
            Text(text = subtitle, style = AppTypography.Subtitle2.regular, color = GrayText)
        }
    }
}