package com.costostudio.ninao.presentation.drawer

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.costostudio.ninao.presentation.drawer.component.DrawerContent
import com.costostudio.ninao.presentation.home.HomeScreen
import com.costostudio.ninao.presentation.profile.ProfileScreen
import com.costostudio.ninao.presentation.settings.SettingsScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerNavigation() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(Screen.Home.route) }

    // Obtenir l'écran actuel
    val currentScreenObj = drawerScreens.find { it.route == currentScreen } ?: Screen.Home

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentScreen = currentScreen,
                onScreenSelected = { screen ->
                    currentScreen = screen.route
                },
                onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentScreenObj.title) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
            // Affichage de l'écran selon la sélection
            when (currentScreen) {
                Screen.Home.route -> HomeScreen()
                Screen.Profile.route -> ProfileScreen()
                Screen.Settings.route -> SettingsScreen()
            }
        }
    }
}