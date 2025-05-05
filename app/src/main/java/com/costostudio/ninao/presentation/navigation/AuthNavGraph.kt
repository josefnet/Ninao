package com.costostudio.ninao.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.costostudio.ninao.presentation.screens.HomeScreen
import com.costostudio.ninao.presentation.screens.LoginScreen
import com.costostudio.ninao.presentation.screens.RegisterScreen
import com.costostudio.ninao.presentation.viewmodel.HomeViewModel
import com.costostudio.ninao.presentation.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun AuthNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") {
            val viewModel: LoginViewModel = koinViewModel()
            LoginScreen(
                onNavigateToHome = { navController.navigate("home_screen") },
                onNavigateToRegister = { navController.navigate("register_screen") },
                viewModel = viewModel
            )
        }
        composable("register_screen") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login_screen") })
        }
        composable("home_screen") {
            val viewModel: HomeViewModel = koinViewModel()
            HomeScreen(
                onNavigateToLogin = { navController.navigate("login_screen") },
                viewModel = viewModel
            )
        }
    }
}