package com.kelompok4.serena.data

import java.util.*

data class Counseling(
    val id: String = UUID.randomUUID().toString(),
    val userEmail: String,
    val counselorName: String,
    val counselorSpecialty: String,
    val counselorImage: Int, // Resource ID untuk foto konselor
    val date: String, // Format: "Senin, 11 September"
    val time: String, // Format: "10:00"
    val status: CounselingStatus = CounselingStatus.SCHEDULED,
    val price: Int,
    val paymentStatus: PaymentStatus = PaymentStatus.PAID,
    val createdAt: Long = System.currentTimeMillis()
)

enum class CounselingStatus {
    SCHEDULED,  // Dijadwalkan
    COMPLETED,  // Selesai
    CANCELLED   // Dibatalkan
}

enum class PaymentStatus {
    PENDING,    // Menunggu pembayaran
    PAID,       // Sudah dibayar
    EXPIRED     // Pembayaran kadaluarsa
}