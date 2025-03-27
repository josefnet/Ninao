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
            val viewModel: LoginViewModel = koinViewModel()
            LoginScreen(navController, viewModel)
        }
        composable("register_screen") {
            val viewModel: RegisterViewModel = koinViewModel()
            RegisterScreen(navController,viewModel)
        }
        composable("home_screen") {
            val viewModel: HomeViewModel = koinViewModel()
            HomeScreen(navController, viewModel)
        }
    }
}