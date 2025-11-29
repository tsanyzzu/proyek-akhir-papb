package com.kelompok4.serena.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelompok4.serena.data.User
import com.kelompok4.serena.data.UserDataManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel : ViewModel() {
    val fullName = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")

    val isPasswordVisible = mutableStateOf(false)
    val isConfirmPasswordVisible = mutableStateOf(false)

    fun onFullNameChange(newValue: String) { fullName.value = newValue }
    fun onEmailChange(newValue: String) { email.value = newValue }
    fun onPasswordChange(newValue: String) { password.value = newValue }
    fun onConfirmPasswordChange(newValue: String) { confirmPassword.value = newValue }

    fun togglePasswordVisibility() { isPasswordVisible.value = !isPasswordVisible.value }
    fun toggleConfirmPasswordVisibility() { isConfirmPasswordVisible.value = !isConfirmPasswordVisible.value }

    /**
     * Registrasi user:
     * - validasi input
     * - cek email terdaftar
     * - cek username unik (query ke Firestore)
     * - panggil UserDataManager.registerUser(...)
     */
    fun onRegisterClick(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val name = fullName.value.trim()
        val mail = email.value.trim()
        val pass = password.value
        val confirm = confirmPassword.value

        if (name.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            onError("Semua field wajib diisi!")
            return
        }

        if (pass != confirm) {
            onError("Password dan konfirmasi tidak cocok!")
            return
        }

        // generate username sederhana (bisa ganti logika username sesuai kebutuhan)
        val username = mail.substringBefore("@")

        viewModelScope.launch {
            try {
                // cek apakah email sudah ada
                val existing = UserDataManager.getUserByEmail(mail)
                if (existing != null) {
                    onError("Email sudah terdaftar!")
                    return@launch
                }

                // cek apakah username sudah dipakai (query ke Firestore)
                val db = FirebaseFirestore.getInstance()
                val usernameQuery = db.collection("users")
                    .whereEqualTo("username", username)
                    .limit(1)
                    .get()
                    .await()

                if (usernameQuery.documents.isNotEmpty()) {
                    onError("Username '$username' sudah dipakai. Coba variasi lain.")
                    return@launch
                }

                // panggil register di UserDataManager
                val success = UserDataManager.registerUser(
                    email = mail,
                    password = pass,
                    fullName = name,
                    username = username
                )

                if (success) {
                    onSuccess()
                } else {
                    onError("Gagal registrasi. Coba lagi.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Terjadi kesalahan saat registrasi.")
            }
        }
    }
}