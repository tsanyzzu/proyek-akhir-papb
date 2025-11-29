package com.kelompok4.serena.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import kotlinx.coroutines.tasks.await

object UserDataManager {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // ================================
    // REGISTER USER (AUTH + FIRESTORE)
    // ================================
    suspend fun registerUser(
        email: String,
        password: String,
        fullName: String,
        username: String
    ): Boolean {
        return try {
            // 1. Buat akun di Firebase Authentication
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return false

            // 2. Buat data user untuk Firestore
            val user = User(
                fullName = fullName,
                username = username,
                email = email,
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
            )

            // 3. Simpan ke Firestore di /users/{uid}
            db.collection("users")
                .document(uid)
                .set(user)
                .await()

            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ================================
    // LOGIN USER
    // ================================
    suspend fun loginUser(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // ================================
    // UPDATE USER DATA
    // ================================
    suspend fun updateUser(
        uid: String,
        newData: Map<String, Any>
    ): Boolean {
        return try {
            db.collection("users")
                .document(uid)
                .update(newData + ("updatedAt" to Timestamp.now()))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // ================================
    // GET USER BY EMAIL
    // ================================
    suspend fun getUserByEmail(email: String): User? {
        return try {
            val query = db.collection("users")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()

            if (query.isEmpty) null
            else query.documents[0].toObject(User::class.java)

        } catch (e: Exception) {
            null
        }
    }
}