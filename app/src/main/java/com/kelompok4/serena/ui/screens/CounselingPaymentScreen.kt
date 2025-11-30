package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounselingPaymentScreen(navController: NavController) { // Nama fungsi disesuaikan & tambah NavController
    var selectedPaymentMethod by remember { mutableStateOf("BCA Virtual Account") }
    var selectedTab by remember { mutableStateOf("Virtual Account") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ringkasan Pembayaran",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) { // Fungsi Back Button
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            ReservationSection()
            Spacer(modifier = Modifier.height(8.dp))
            PaymentSummarySection()
            Spacer(modifier = Modifier.height(8.dp))
            PaymentMethodSection(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                selectedPaymentMethod = selectedPaymentMethod,
                onPaymentMethodSelected = { selectedPaymentMethod = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            BottomPaymentSection()
        }
    }
}

// ... (Bagian ReservationSection, PaymentSummarySection, dll tetap sama seperti kode Anda sebelumnya)
// Pastikan semua komponen pendukung (ReservationSection, PaymentSummarySection, dll) ada di file ini.
@Composable
fun ReservationSection() {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Reservasi", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Mohon periksa kembali detail yang dimasukkan.", fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.Top) {
                Box(modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE3F2FD)))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Dr. Laura Azzura, S.Psi.", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Sen, Sep 11, 10.00", fontSize = 13.sp, color = Color.Gray)
                    Text("S1 Psikologi Universitas Brawijaya", fontSize = 13.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun PaymentSummarySection() {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Ringkasan pembayaran", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            PaymentRow("Harga", "10.000")
            PaymentRow("Biaya Servis", "6.000")
            PaymentRow("Diskon Pengguna Pertama", "-16.000", true)
            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Rp0", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PaymentRow(label: String, amount: String, isDiscount: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 14.sp, color = Color.Gray)
        Text(amount, fontSize = 14.sp, color = if (isDiscount) Color(0xFF2D7D5F) else Color.Gray)
    }
}

@Composable
fun PaymentMethodSection(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    selectedPaymentMethod: String,
    onPaymentMethodSelected: (String) -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pilih metode pembayaran", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PaymentMethodTab("E-Wallet", selectedTab == "E-Wallet") { onTabSelected("E-Wallet") }
                PaymentMethodTab("Virtual Account", selectedTab == "Virtual Account") { onTabSelected("Virtual Account") }
            }
            Spacer(modifier = Modifier.height(16.dp))
            PaymentOption("BCA Virtual Account", "BCA", selectedPaymentMethod == "BCA Virtual Account") { onPaymentMethodSelected("BCA Virtual Account") }
            Spacer(modifier = Modifier.height(12.dp))
            PaymentOption("BNI Virtual Account", "BNI", selectedPaymentMethod == "BNI Virtual Account") { onPaymentMethodSelected("BNI Virtual Account") }
        }
    }
}

@Composable
fun PaymentMethodTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF368743) else Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF2D7D5F) else Color(0xFFE0E0E0))
    ) {
        Text(text, fontSize = 13.sp, color = if (isSelected) Color.White else Color.Gray, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
    }
}

@Composable
fun PaymentOption(bankName: String, icon: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp)), contentAlignment = Alignment.Center) {
                    Text(icon, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(bankName, fontSize = 14.sp)
            }
            RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF2D7D5F)))
        }
    }
}

@Composable
fun BottomPaymentSection() {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, tonalElevation = 8.dp) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Total yang harus dibayar", fontSize = 12.sp, color = Color.Gray)
                Text("Rp0", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(onClick = { }, modifier = Modifier.width(140.dp).height(48.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF368743))) {
                Text("Bayar", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Preview
@Composable
fun PaymentSummaryScreenPreview() {
    CounselingPaymentScreen(rememberNavController())
}