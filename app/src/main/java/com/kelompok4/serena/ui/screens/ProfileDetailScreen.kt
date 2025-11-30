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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kelompok4.serena.R
import com.kelompok4.serena.ui.theme.AppTypography
import com.kelompok4.serena.ui.theme.Primary500
import com.kelompok4.serena.ui.viewmodel.ProfileViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun ProfileDetailScreen(
    navController: NavController,
    userEmail: String,
    vm: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val user by vm.user.collectAsState()

<<<<<<< Updated upstream
=======
    // local state untuk dialog edit email
    var isUpdatingEmail by remember { mutableStateOf(false) }
    var showEditEmailDialog by remember { mutableStateOf(false) }
    var newEmailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            isUploading = true
            vm.uploadProfilePhoto(uri) { success, message ->
                isUploading = false
                if (success) {
                    Toast.makeText(context, "Foto profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    selectedImageUri = null
                } else {
                    Toast.makeText(context, message ?: "Gagal upload foto", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
>>>>>>> Stashed changes
    LaunchedEffect(userEmail) {
        vm.loadUser(context, userEmail)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))
<<<<<<< Updated upstream
        Image(
            painter = painterResource(id = user?.profilePictureRes ?: R.drawable.default_profile),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
                .clickable { /* aksi ganti foto profil */ }
        )
=======

        // Avatar
        val avatarModel: Any? = selectedImageUri ?: user?.profilePhotoUrl
        if (avatarModel != null && avatarModel.toString().isNotBlank()) {
            AsyncImage(
                model = avatarModel,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
            )

        } else {
            Image(
                painter = painterResource(id = R.drawable.default_profile),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clickable { pickImageLauncher.launch("image/*") }
            )
        }

        if (isUploading) {
            Spacer(Modifier.height(8.dp))
            CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 2.dp)
        }

>>>>>>> Stashed changes
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Ganti Foto",
            style = AppTypography.Body1.medium,
            color = Primary500,
            modifier = Modifier.clickable {
                pickImageLauncher.launch("image/*")
            }
        )
        Spacer(Modifier.height(20.dp))

        // Card Username
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

        // Card Nama Lengkap
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

        // Card Email
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
                    onEdit = { navController.navigate("edit_value/$userEmail/email") }
                )
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