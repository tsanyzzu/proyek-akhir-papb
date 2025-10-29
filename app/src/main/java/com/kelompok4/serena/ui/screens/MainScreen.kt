package com.kelompok4.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kelompok4.serena.ui.navigation.Routes
import com.kelompok4.serena.ui.navigation.getAllBottomNavItems
import com.kelompok4.serena.ui.theme.*

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = Color.White
    ) { paddingValues ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        // --- TAMBAHKAN MODIFIER INI UNTUK PADDING BAWAH ---
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        getAllBottomNavItems().forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == item.route
            } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        // HAPUS .fillMaxHeight() DARI SINI
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(3.dp)
                                    .background(
                                        color = Primary500,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        } else {
                            // Tambahkan Spacer kosong agar tinggi tetap konsisten
                            Spacer(modifier = Modifier.height(9.dp))
                        }

                        Icon(
                            painter = painterResource(
                                id = if (isSelected) item.selectedIcon else item.icon
                            ),
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        style = AppTypography.Button.medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary500,
                    selectedTextColor = Primary500,
                    unselectedIconColor = DisabledTextGray,
                    unselectedTextColor = DisabledTextGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            HomeScreen()
        }
        composable(Routes.SELF_CARE) {
            SelfCareScreen()
        }
        composable(Routes.KONSELING) {
            KonselingScreen()
        }
        composable(Routes.PROFIL) {
            ProfilScreen()
        }
    }
}