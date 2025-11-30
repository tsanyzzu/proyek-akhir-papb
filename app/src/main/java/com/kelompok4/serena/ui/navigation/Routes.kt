package com.kelompok4.serena.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.serena.ui.screens.*
import androidx.navigation.*

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val SELF_CARE = "self_care"
    const val KONSELING = "konseling"
    const val PROFIL = "profil/{email}"
    const val MAIN = "main/{email}"
    const val EDIT_VALUE = "edit_value/{email}/{field}"
    const val SUCCESS_PROFILE = "success_profile"
    const val COUNSELING_SCHEDULE = "counseling_schedule"
    const val SelfCare = "selfCare"
    const val Articles = "articles"
    const val ArticleDetail = "articleDetail/{id}"
    const val Activities = "activities"
    const val ActivityDetail = "activityDetail/{id}"
    const val SleepQuality = "sleepQuality"
    const val SleepHistory = "sleepHistory"

}


