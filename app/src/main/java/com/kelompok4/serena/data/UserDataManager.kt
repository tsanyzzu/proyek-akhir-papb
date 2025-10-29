package com.kelompok4.serena.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object UserDataManager {
    private const val FILE_NAME = "users.json"

    private fun getFile(context: Context): File {
        return File(context.filesDir, FILE_NAME)
    }

    private fun readUsers(context: Context): MutableList<User> {
        val file = getFile(context)
        if (!file.exists()) return mutableListOf()
        val json = file.readText()
        if (json.isEmpty()) return mutableListOf()
        val type = object : TypeToken<MutableList<User>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun writeUsers(context: Context, users: List<User>) {
        val file = getFile(context)
        val json = Gson().toJson(users)
        file.writeText(json)
    }

    fun registerUser(context: Context, user: User): Boolean {
        val users = readUsers(context)
        if (users.any { it.email == user.email }) return false // email sudah terdaftar
        users.add(user)
        writeUsers(context, users)
        return true
    }

    fun loginUser(context: Context, email: String, password: String): User? {
        val users = readUsers(context)
        return users.find { it.email == email && it.password == password }
    }

    fun updateUser(context: Context, updatedUser: User) {
        val users = readUsers(context)
        val index = users.indexOfFirst { it.email == updatedUser.email }
        if (index != -1) {
            users[index] = updatedUser
            writeUsers(context, users)
        }
    }

    fun getUserByEmail(context: Context, email: String): User? {
        return readUsers(context).find { it.email == email }
    }
}
