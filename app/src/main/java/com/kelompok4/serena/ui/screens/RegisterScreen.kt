package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kelompok4.serena.R
import com.kelompok4.serena.ui.components.AppButton
import com.kelompok4.serena.ui.components.ButtonType
import com.kelompok4.serena.ui.theme.*
import com.kelompok4.serena.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    navController: NavController
) {
    val fullName = viewModel.fullName.value
    val email = viewModel.email.value
    val password = viewModel.password.value
    val confirmPassword = viewModel.confirmPassword.value
    val isPasswordVisible = viewModel.isPasswordVisible.value
    val isConfirmPasswordVisible = viewModel.isConfirmPasswordVisible.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.serena_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Judul dan Subjudul
                Text("Buat Akun", style = AppTypography.H4.bold, color = Color.Black)
                Text(
                    "Daftarkan akun Anda untuk mengakses aplikasi",
                    style = AppTypography.Subtitle2.regular,
                    color = GrayText
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== Input Fields =====
                OutlinedTextField(
                    value = fullName,
                    onValueChange = viewModel::onFullNameChange,
                    label = { Text("Nama Lengkap", style = AppTypography.Subtitle2.regular) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = viewModel::onEmailChange,
                    label = { Text("Email", style = AppTypography.Subtitle2.regular) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Kata Sandi", style = AppTypography.Subtitle2.regular) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (isPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                        IconButton(onClick = viewModel::togglePasswordVisibility) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = if (isPasswordVisible) "Sembunyikan Password" else "Tampilkan Password"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = { Text("Konfirmasi Kata Sandi", style = AppTypography.Subtitle2.regular) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (isConfirmPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                        IconButton(onClick = viewModel::toggleConfirmPasswordVisibility) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = if (isConfirmPasswordVisible) "Sembunyikan Password" else "Tampilkan Password"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Ketentuan Layanan & Privasi
                Text(
                    text = buildAnnotatedString {
                        append("Dengan mendaftar, Anda menyetujui ")
                        withStyle(style = SpanStyle(color = Primary500)) { append("Ketentuan Layanan") }
                        append(" dan ")
                        withStyle(style = SpanStyle(color = Primary500)) { append("Kebijakan Privasi") }
                        append(" Serena.")
                    },
                    style = AppTypography.Subtitle2.regular,
                    color = GrayText
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tombol Daftar
                AppButton(
                    text = "Daftar",
                    onClick = viewModel::onRegisterClick,
                    modifier = Modifier.fillMaxWidth(),
                    buttonType = ButtonType.PRIMARY,
                    enabled = fullName.isNotBlank() && email.isNotBlank() &&
                            password.isNotBlank() && confirmPassword.isNotBlank()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Divider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DisabledGray, thickness = 1.dp)
                    Text("  atau daftar dengan  ", style = AppTypography.Subtitle2.regular, color = GrayText)
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DisabledGray, thickness = 1.dp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Sosial Media
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val socialIcons = listOf(
                        Triple("Facebook", R.drawable.ic_facebook, viewModel::onRegisterClick),
                        Triple("Google", R.drawable.ic_google, viewModel::onRegisterClick),
                        Triple("Apple", R.drawable.ic_apple, viewModel::onRegisterClick)
                    )

                    socialIcons.forEachIndexed { index, (name, icon, action) ->
                        val interactionSource = remember { MutableInteractionSource() }
                        val isPressed by interactionSource.collectIsPressedAsState()
                        val bgColor = if (isPressed) DisabledGray.copy(alpha = 0.2f) else Color.White

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(bgColor, CircleShape)
                                .border(1.dp, DisabledGray.copy(alpha = 0.4f), CircleShape)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = action
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = icon),
                                contentDescription = name,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        if (index < socialIcons.lastIndex) {
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                }
            }

            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sudah punya akun? ",
                    style = AppTypography.Subtitle2.regular,
                    color = GrayText
                )
                Text(
                    text = "Masuk",
                    style = AppTypography.Subtitle2.medium,
                    color = Primary500,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
            }
        }
    }
}
