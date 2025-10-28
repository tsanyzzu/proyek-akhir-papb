package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kelompok4.serena.R
import com.kelompok4.serena.ui.components.AppButton
import com.kelompok4.serena.ui.components.ButtonType
import com.kelompok4.serena.ui.theme.*
import com.kelompok4.serena.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToRegister: () -> Unit = {}
) {
    val email = viewModel.email.value
    val password = viewModel.password.value
    val isPasswordVisible = viewModel.isPasswordVisible.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.serena_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(100.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Selamat Datang!",
                    style = AppTypography.H3.bold,
                    color = Color.Black
                )

                Text(
                    text = "Masuk ke akun Anda untuk melanjutkan",
                    style = AppTypography.Body1.regular,
                    color = GrayText
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = viewModel::onEmailChange,
                    label = { Text("Email", style = AppTypography.Body1.regular) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Kata Sandi", style = AppTypography.Body1.regular) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (isPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                        IconButton(
                            onClick = viewModel::togglePasswordVisibility,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = if (isPasswordVisible) "Sembunyikan Password" else "Tampilkan Password",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Forgot Password
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(
                        text = "Lupa Password?",
                        modifier = Modifier.clickable { /* TODO: forgot password */ },
                        style = AppTypography.Subtitle2.medium,
                        color = Primary500
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button (disabled jika belum diisi)
                AppButton(
                    text = "Masuk",
                    onClick = viewModel::onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    buttonType = ButtonType.PRIMARY,
                    enabled = email.isNotBlank() && password.isNotBlank()
                )

                Spacer(modifier = Modifier.height(56.dp))

                // Garis kiri-kanan + "Atau masuk dengan"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = DisabledGray,
                        thickness = DividerDefaults.Thickness
                    )

                    Text(
                        text = "  Atau masuk dengan  ",
                        style = AppTypography.Body1.regular,
                        color = GrayText
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = DisabledGray,
                        thickness = DividerDefaults.Thickness
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val socialIcons = listOf(
                        Triple("Facebook", R.drawable.ic_facebook, viewModel::onFacebookLogin),
                        Triple("Google", R.drawable.ic_google, viewModel::onGoogleLogin),
                        Triple("Apple", R.drawable.ic_apple, viewModel::onAppleLogin)
                    )

                    socialIcons.forEachIndexed { index, (name, icon, action) ->
                        val interactionSource = remember { MutableInteractionSource() }
                        val isPressed by interactionSource.collectIsPressedAsState()

                        // Ubah warna background berdasarkan status tekan
                        val backgroundColor =
                            if (isPressed) DisabledGray.copy(alpha = 0.2f) else Color.White

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(backgroundColor, CircleShape)
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
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (index < socialIcons.lastIndex) {
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                }
            }

            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Belum punya akun? ",
                    style = AppTypography.Body1.regular,
                    color = GrayText
                )
                Text(
                    text = "Daftar",
                    style = AppTypography.Body1.medium,
                    color = Primary500,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}