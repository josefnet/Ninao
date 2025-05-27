package com.costostudio.ninao.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
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
fun AuthNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val navigator = remember(navController) { AuthNavigatorImpl(navController) }

    NavHost(navController, startDestination = AppDestinations.LOGIN) {
        composable(AppDestinations.LOGIN) {
            val viewModel: LoginViewModel = koinViewModel()
            LoginScreen(viewModel, navigator)
        }
        composable(AppDestinations.HOME) {
            val viewModel: HomeViewModel = koinViewModel()
            HomeScreen(viewModel, navigator)
        }
        composable(AppDestinations.REGISTER) {
            val viewModel: RegisterViewModel = koinViewModel()
            RegisterScreen(viewModel, navigator)
        }

    }
}