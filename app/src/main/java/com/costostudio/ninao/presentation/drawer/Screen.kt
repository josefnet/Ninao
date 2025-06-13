package com.costostudio.ninao.presentation.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val drawerScreens = listOf(
    Screen.Home,
    Screen.Profile,
    Screen.Settings
)