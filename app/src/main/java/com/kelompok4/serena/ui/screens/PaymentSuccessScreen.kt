package com.kelompok4.serena.ui.screens

import androidx.compose. foundation.Image
import androidx.compose.foundation.background
import androidx. compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose. ui.Modifier
import androidx. compose.ui.graphics.Color
import androidx.compose. ui.res.painterResource
import androidx. compose.ui.text.style. TextAlign
import androidx.compose. ui.unit.dp
import androidx.navigation.NavController
import com.kelompok4.serena.R
import com.kelompok4.serena.ui.components. AppButton
import com.kelompok4.serena.ui.components.ButtonType
import com.kelompok4.serena.ui.theme.*
import com.kelompok4. serena.data. Counseling
import com.kelompok4.serena.data. CounselingDataManager
import androidx.compose.ui.platform.LocalContext

@Composable
fun PaymentSuccessScreen(
    navController: NavController,
    userEmail: String,
    counselorName: String,
    counselorSpecialty: String,
    counselorImage: Int,
    date: String,
    time: String,
    price: Int
) {
    val context = LocalContext.current
    // Simpan data konseling saat screen pertama kali dibuka
    LaunchedEffect(Unit) {
        val counseling = Counseling(
            userEmail = userEmail,
            counselorName = counselorName,
            counselorSpecialty = counselorSpecialty,
            counselorImage = counselorImage,
            date = date,
            time = time,
            price = price
        )
        CounselingDataManager.saveCounseling(context, counseling)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier. weight(1f))

            // Success Illustration
            Image(
                painter = painterResource(id = R. drawable.serena_logo),
                contentDescription = "Success Illustration",
                modifier = Modifier
                    . size(280.dp)
                    .padding(bottom = 32.dp)
            )

            // Success Title
            Text(
                text = "Selamat! ",
                style = AppTypography.H3.bold,
                color = Primary500,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier. height(16.dp))

            // Success Message
            Text(
                text = "Proses akan segera diproses oleh tim kami.  Terima kasih telah mempercayakan perjalanan kesehatan mentalmu bersama kami! ",
                style = AppTypography.Body1.regular,
                color = GrayText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // Bottom Button
        AppButton(
            text = "Kembali ke konseling",
            onClick = {
                navController.navigate("konseling") {
                    popUpTo("konseling") {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                . align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            buttonType = ButtonType.PRIMARY
        )
    }
}