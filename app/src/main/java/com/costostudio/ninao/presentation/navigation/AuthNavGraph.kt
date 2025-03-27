package com.costostudio.ninao.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.costostudio.ninao.presentation.screens.HomeScreen
import com.costostudio.ninao.presentation.screens.LoginScreen
import com.costostudio.ninao.presentation.screens.RegisterScreen
import com.costostudio.ninao.presentation.viewmodel.HomeViewModel
import com.costostudio.ninao.presentation.viewmodel.LoginViewModel
import com.costostudio.ninao.presentation.viewmodel.RegisterViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") {
            /*val viewModel: LoginViewModel = koinViewModel()
            LoginScreen(navController, viewModel)*/
            LoginScreen(
                onNavigateToHome = { navController.navigate("home_screen") },
                onNavigateToRegister = { navController.navigate("register_screen") }
            )
        }
        composable("register_screen") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login_screen") })
        }
        composable("home_screen") {

            HomeScreen(
                onNavigateToLogin = { navController.navigate("login_screen") }
            )
        }
    }
}