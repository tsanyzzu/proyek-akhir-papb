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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kelompok4.serena.ui.navigation.BottomNavItem
import com.kelompok4.serena.ui.navigation.Routes
import com.kelompok4.serena.ui.navigation.getAllBottomNavItems
import com.kelompok4.serena.ui.theme.*
import com.example.serena.ui.screens.ActivityDetailScreen
import com.example.serena.ui.screens.ArticleDetailScreen
import com.example.serena.ui.screens.ArticleListScreen
import com.example.serena.ui.screens.SelfCareScreen

@Composable
fun MainScreen(userEmail: String) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, userEmail = userEmail)
        },
        containerColor = Color.White
    ) { paddingValues ->
        NavigationGraph(
            navController = navController,
            userEmail = userEmail,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, userEmail: String) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        getAllBottomNavItems().forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route?.startsWith(item.route.substringBefore("/{")) == true
            } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    val route = if (item == BottomNavItem.Profil) {
                        "profil/$userEmail"
                    } else item.route

                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
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
                label = { Text(text = item.title, style = AppTypography.Button.medium) },
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
    userEmail: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        // Home Screen
        composable(Routes.HOME) {
            HomeScreen(navController = navController, userEmail = userEmail)
        }

        // Self Care Screen
        composable(Routes.SELF_CARE) {
            SelfCareScreen(navController = navController)
        }

        // Counseling Screen
        composable(Routes.KONSELING) {
            CounselingScreen(navController = navController)
        }

        // Profile Screen
        composable("profil/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ProfileScreen(navController = navController, userEmail = email)
        }

        // Profile Detail Screen
        composable("profile_detail/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ProfileDetailScreen(navController = navController, userEmail = email)
        }

        // Edit Profile Value Screen
        composable("edit_value/{email}/{field}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val field = backStackEntry.arguments?.getString("field") ?: ""
            EditValueScreen(navController = navController, userEmail = email, field = field)
        }

        // Article Detail Screen
        composable("articleDetail/{id}") { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            ArticleDetailScreen(navController = navController, articleId = articleId)
        }

        // Activity Detail Screen
        composable("activityDetail/{id}") { backStackEntry ->
            val activityId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            ActivityDetailScreen(navController = navController, activityId = activityId)
        }

        // Sleep Quality Screen
        composable(Routes.SleepQuality) {
            SleepQualityScreen(navController = navController)
        }

        // Sleep History Screen
        composable(Routes.SleepHistory) {
            SleepHistoryScreen(navController = navController)
        }

        // Success Profile Screen
        composable("success_profile") {
            SuccessProfileScreen(navController = navController)
        }

        // ========== JOURNAL ROUTES ==========

        // Add Journal Screen (Create new journal)
        composable(
            route = "add_journal/{email}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            AddJournalScreen(
                navController = navController,
                userEmail = email
            )
        }

        // Edit Journal Screen (Edit existing journal)
        composable(
            route = "add_journal/{email}/{journalId}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("journalId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val journalId = backStackEntry.arguments?.getString("journalId")
            AddJournalScreen(
                navController = navController,
                userEmail = email,
                journalId = journalId
            )
        }

        // Journal List Screen (Show all journals)
        composable(
            route = "journal_list/{email}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            JournalListScreen(
                navController = navController,
                userEmail = email
            )
        }

        // Journal Detail Screen (View single journal)
        composable(
            route = "journal_detail/{journalId}",
            arguments = listOf(
                navArgument("journalId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val journalId = backStackEntry.arguments?.getString("journalId") ?: ""
            JournalDetailScreen(
                navController = navController,
                journalId = journalId
            )
        }
    }
}