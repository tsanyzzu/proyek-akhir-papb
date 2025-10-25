package com.kelompok4.serena.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class LoginViewModel : ViewModel() {
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isPasswordVisible = mutableStateOf(false)
    val isPasswordVisible: State<Boolean> = _isPasswordVisible

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun onLoginClick() {
        println("Email: ${_email.value}, Password: ${_password.value}")
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
