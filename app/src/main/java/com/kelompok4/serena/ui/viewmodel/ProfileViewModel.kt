package com.kelompok4.serena.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelompok4.serena.data.User
import com.kelompok4.serena.data.UserDataManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUser(context: Context, email: String) {
        viewModelScope.launch {
            _user.value = UserDataManager.getUserByEmail(context, email)
        }
    }

    fun updateUser(context: Context, updated: User) {
        viewModelScope.launch {
            UserDataManager.updateUser(context, updated)
            _user.value = updated
        }
    }
}