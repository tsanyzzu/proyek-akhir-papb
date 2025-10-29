package com.kelompok4.serena.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kelompok4.serena.R
import com.kelompok4.serena.data.User
import com.kelompok4.serena.data.UserDataManager

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

    fun onRegisterClick(context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
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

        val username = mail.substringBefore("@")
        val newUser = User(
            fullName = name,
            email = mail,
            username = username,
            password = pass,
            profilePictureRes = R.drawable.default_profile // default dari drawable
        )

        val success = UserDataManager.registerUser(context, newUser)
        if (success) onSuccess() else onError("Email sudah terdaftar!")
    }
}
