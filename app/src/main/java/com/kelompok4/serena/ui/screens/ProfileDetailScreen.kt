package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok4.serena.R
import com.kelompok4.serena.ui.theme.AppTypography
import com.kelompok4.serena.ui.theme.Primary500

@Composable
fun ProfileDetailScreen(navController: NavController, userEmail: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.default_profile),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
                .clickable { /* Ganti foto profil */ }
        )
        Spacer(Modifier.height(8.dp))
        Text(text = "Ganti Foto", style = AppTypography.Body1.medium, color = Primary500)

        Spacer(Modifier.height(20.dp))

        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                ProfileFieldRow(label = "Username", value = "Ahsana", onEdit = {
                    navController.navigate("edit_value/$userEmail/username")
                })
                Spacer(Modifier.height(8.dp))
                ProfileFieldRow(label = "Nama Lengkap", value = "Ahsana Iklila", onEdit = {
                    navController.navigate("edit_value/$userEmail/fullname")
                })
                Spacer(Modifier.height(8.dp))
                ProfileFieldRow(label = "Email", value = userEmail, onEdit = {
                    navController.navigate("edit_value/$userEmail/email")
                })
            }
        }
    }
}

@Composable
private fun ProfileFieldRow(label: String, value: String, onEdit: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = AppTypography.Subtitle2.regular)
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
