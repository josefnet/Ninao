package com.costostudio.ninao.presentation.navigation

import androidx.navigation.NavHostController

interface AuthNavigator {
    fun navigateToHome()
    fun navigateToLogin()
    fun navigateToRegister()
}

class AuthNavigatorImpl(
    private val navController: NavHostController
) : AuthNavigator {
    override fun navigateToHome() = navController.navigate(AppDestinations.HOME)
    override fun navigateToLogin() = navController.navigate(AppDestinations.LOGIN)
    override fun navigateToRegister() = navController.navigate(AppDestinations.REGISTER)

}