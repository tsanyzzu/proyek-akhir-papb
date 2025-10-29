package com.kelompok4.serena.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kelompok4.serena.data.User
import com.kelompok4.serena.data.UserDataManager

class LoginViewModel : ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val isPasswordVisible = mutableStateOf(false)
    val loggedInUser = mutableStateOf<User?>(null)

    fun onEmailChange(value: String) { email.value = value }
    fun onPasswordChange(value: String) { password.value = value }
    fun togglePasswordVisibility() { isPasswordVisible.value = !isPasswordVisible.value }

    fun onLoginClick(context: Context, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        val mail = email.value.trim()
        val pass = password.value

        val user = UserDataManager.loginUser(context, mail, pass)
        if (user != null) {
            loggedInUser.value = user
            onSuccess(user)
        } else {
            onError("Email atau kata sandi salah.")
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
