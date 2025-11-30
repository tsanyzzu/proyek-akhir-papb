package com.kelompok4.serena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kelompok4.serena.ui.navigation.RootNavGraph
import com.kelompok4.serena.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // TODO: ganti dengan data dari SharedPreferences / DataStore
            val isFirstTime = true
            val isLoggedIn = false

            RootNavGraph(
                navController = navController,
                isFirstTime = isFirstTime,
                isLoggedIn = isLoggedIn
            )
        }
    }
}

// Komposables ini hanya untuk memastikan terdeteksi oleh RootNavGraph
@Composable
fun SelfCareScreen(navController: NavHostController) { /* sudah diimplementasi di screens */ }

@Composable
fun ArticleListScreen(navController: NavHostController) { /* sudah diimplementasi di screens */ }

@Composable
fun ActivityDetailScreen(navController: NavHostController, activityId: Int) { /* sudah diimplementasi di screens */ }

@Composable
fun ArticleDetailScreen(navController: NavHostController, articleId: Int) { /* sudah diimplementasi di screens */ }
