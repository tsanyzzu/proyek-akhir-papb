package com.kelompok4.serena.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelompok4.serena.data.User
import com.kelompok4.serena.data.UserDataManager
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val isPasswordVisible = mutableStateOf(false)
    val loggedInUser = mutableStateOf<User?>(null)

    fun onEmailChange(value: String) { email.value = value }
    fun onPasswordChange(value: String) { password.value = value }
    fun togglePasswordVisibility() { isPasswordVisible.value = !isPasswordVisible.value }

    /**
     * Menggunakan callback onSuccess/onError untuk menginformasikan UI.
     * Tidak membutuhkan Context.
     */
    fun onLoginClick(onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        val mail = email.value.trim()
        val pass = password.value

        if (mail.isEmpty() || pass.isEmpty()) {
            onError("Email dan password harus diisi.")
            return
        }

        viewModelScope.launch {
            try {
                val ok = UserDataManager.loginUser(mail, pass)
                if (ok) {
                    // Ambil data user dari koleksi users (berdasarkan email)
                    val user = UserDataManager.getUserByEmail(mail)
                    if (user != null) {
                        loggedInUser.value = user
                        onSuccess(user)
                    } else {
                        // Login berhasil di Auth, tapi data user di Firestore tidak ditemukan
                        onError("Login berhasil, tapi data pengguna tidak ditemukan.")
                    }
                } else {
                    onError("Email atau kata sandi salah.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Terjadi kesalahan saat login.")
            }
        }
    }

    fun onGoogleLogin() {
        println("Google login clicked")
    }

    fun onFacebookLogin() {
        println("Facebook login clicked")
    }

    fun onAppleLogin() {
        println("Apple login clicked")
    }
}