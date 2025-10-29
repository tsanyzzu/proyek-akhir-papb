package com.kelompok4.serena.ui.navigation

import androidx.annotation.DrawableRes
import com.kelompok4.serena.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int
) {
    object Beranda : BottomNavItem(
        route = Routes.HOME,
        title = "Beranda",
        icon = R.drawable.ic_home_outline,
        selectedIcon = R.drawable.ic_home_filled
    )

    object SelfCare : BottomNavItem(
        route = Routes.SELF_CARE,
        title = "Self Care",
        icon = R.drawable.ic_selfcare_outline,
        selectedIcon = R.drawable.ic_selfcare_filled
    )

    object Konseling : BottomNavItem(
        route = Routes.KONSELING,
        title = "Konseling",
        icon = R.drawable.ic_konseling_outline,
        selectedIcon = R.drawable.ic_konseling_filled
    )

    object Profil : BottomNavItem(
        route = Routes.PROFIL,
        title = "Profil",
        icon = R.drawable.ic_profil_outline,
        selectedIcon = R.drawable.ic_profil_filled
    )
}

fun getAllBottomNavItems() = listOf(
    BottomNavItem.Beranda,
    BottomNavItem.SelfCare,
    BottomNavItem.Konseling,
    BottomNavItem.Profil
)