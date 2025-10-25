package com.kelompok4.serena.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    // --- State untuk input field ---
    val fullName = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")

    // --- State untuk visibilitas password ---
    val isPasswordVisible = mutableStateOf(false)
    val isConfirmPasswordVisible = mutableStateOf(false)

    // --- Aksi untuk setiap perubahan input ---
    fun onFullNameChange(newValue: String) {
        fullName.value = newValue
    }

    fun onEmailChange(newValue: String) {
        email.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        password.value = newValue
    }

    fun onConfirmPasswordChange(newValue: String) {
        confirmPassword.value = newValue
    }

    // --- Toggle visibility password ---
    fun togglePasswordVisibility() {
        isPasswordVisible.value = !isPasswordVisible.value
    }

    fun toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible.value = !isConfirmPasswordVisible.value
    }

    // --- Aksi ketika tombol daftar diklik ---
    fun onRegisterClick() {
        val name = fullName.value.trim()
        val mail = email.value.trim()
        val pass = password.value
        val confirm = confirmPassword.value

        // Contoh validasi sederhana
        if (name.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            println("⚠️ Semua field wajib diisi!")
            return
        }

        if (pass != confirm) {
            println("⚠️ Password dan konfirmasi tidak cocok!")
            return
        }

        // Simulasi registrasi
        println("✅ Registrasi berhasil untuk: $name <$mail>")
    }
}
