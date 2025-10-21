package com.kelompok4.serena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.kelompok4.serena.ui.navigation.RootNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // Misal status disimpan di SharedPreferences / DataStore
            val isFirstTime = true   // contoh dummy
            val isLoggedIn = false   // contoh dummy

            RootNavGraph(
                navController = navController,
                isFirstTime = isFirstTime,
                isLoggedIn = isLoggedIn
            )
        }
    }
}