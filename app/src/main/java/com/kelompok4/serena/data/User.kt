package com.kelompok4.serena.data

import com.google.firebase.Timestamp

data class User(
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val profilePhotoUrl: String = "",
    val profilePhotoUpdatedAt: Timestamp? = null,
    val preferences: Map<String, Any> = mapOf(
        "timezone" to "Asia/Jakarta",
        "notificationsEnabled" to true,
        "language" to "id"
    ),
    val lastPasswordResetRequestedAt: Timestamp? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
)
