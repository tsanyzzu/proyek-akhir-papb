package com.kelompok4.serena.ui.screens

import androidx.compose.foundation. background
import androidx.compose.foundation. border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material. icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material. icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui. platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui. text.AnnotatedString
import androidx.compose.ui.text. style.TextAlign
import androidx. compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation. NavController
import com.kelompok4.serena.R
import com.kelompok4. serena.ui.components.AppButton
import com.kelompok4.serena.ui.components.ButtonType
import com.kelompok4.serena.ui.theme.*
import android.widget.Toast
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentConfirmationScreen(
    navController: NavController,
    totalAmount: Int = 0
) {
    val context = LocalContext. current
    val clipboardManager = LocalClipboardManager. current
    val virtualAccountNumber = "9374912347112311"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TopAppBar
        TopAppBar(
            title = {
                Text(
                    text = "Selesaikan Pembayaran",
                    style = AppTypography.H6.bold,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                . padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier. height(16.dp))

            // Total dibayar Section
            Row(
                modifier = Modifier. fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total dibayar",
                    style = AppTypography.Subtitle2.regular,
                    color = GrayText
                )
                Text(
                    text = "Rp$totalAmount",
                    style = AppTypography.Body1.bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16. dp))

            // Warning Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Secondary100
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Placeholder icon - ganti dengan ic_info_circle nanti
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Warning",
                        tint = Secondary700,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier. width(8.dp))
                    Text(
                        text = "Tetap di halaman sampai pembayaran selesai",
                        style = AppTypography.Subtitle2.medium,
                        color = Secondary900
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Countdown Timer
            Row(
                modifier = Modifier. fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Complete payment before",
                    style = AppTypography.Body1.regular,
                    color = GrayText
                )
                Text(
                    text = "09:14",
                    style = AppTypography.Body1.bold,
                    color = Primary500
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "12 Sep 2025, 09:50 WIB",
                style = AppTypography.Body1.regular,
                color = Color.Black
            )

            Spacer(modifier = Modifier. height(24.dp))

            // BCA Virtual Account Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color. White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16. dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BCA Virtual Account",
                            style = AppTypography.Body1.bold,
                            color = Color.Black
                        )
                        // Placeholder untuk BCA Logo - ganti dengan ic_bca_logo nanti
                        Text(
                            text = "BCA",
                            style = AppTypography.H6.bold,
                            color = TertiaryBlue500
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Virtual Account Number
                    Text(
                        text = virtualAccountNumber,
                        style = AppTypography.H6.bold,
                        color = Color.Black,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Nomor virtual account",
                            style = AppTypography.Body1.regular,
                            color = GrayText
                        )

                        // Copy Button
                        Row(
                            modifier = Modifier
                                .clickable {
                                    clipboardManager. setText(AnnotatedString(virtualAccountNumber))
                                    Toast.makeText(
                                        context,
                                        "Nomor rekening disalin",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                . padding(4.dp),
                            verticalAlignment = Alignment. CenterVertically
                        ) {
                            Text(
                                text = "Copy",
                                style = AppTypography. Subtitle2.medium,
                                color = Primary500
                            )
                            Spacer(modifier = Modifier. width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = Primary500,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier. weight(1f))

            // Bottom Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16. dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Batal Button (Outlined)
                AppButton(
                    text = "Batal",
                    onClick = {
                        navController. navigateUp()
                    },
                    modifier = Modifier. weight(1f),
                    buttonType = ButtonType.SECONDARY
                )

                // Konfirmasi Button
                AppButton(
                    text = "Konfirmasi",
                    onClick = {
                        // TODO: Handle payment confirmation
                        Toast.makeText(
                            context,
                            "Pembayaran dikonfirmasi",
                            Toast.LENGTH_SHORT
                        ). show()
                    },
                    modifier = Modifier.weight(1f),
                    buttonType = ButtonType.PRIMARY
                )
            }
        }
    }
}

@Preview
@Composable
fun PaymentConfirmationScreenPreview() {
    PaymentConfirmationScreen(
        navController = NavController(LocalContext.current))
}