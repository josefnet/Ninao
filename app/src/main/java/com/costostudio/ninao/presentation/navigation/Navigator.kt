package com.costostudio.ninao.presentation.navigation

import androidx.navigation.NavHostController

interface Navigator {
    fun navigateToHome()
    fun navigateToLogin()
    fun navigateToRegister()
}

class NavigatorImpl(
    private val navController: NavHostController
) : Navigator {
    override fun navigateToHome() = navController.navigate(AppDestinations.HOME)
    override fun navigateToLogin() = navController.navigate(AppDestinations.LOGIN)
    override fun navigateToRegister() = navController.navigate(AppDestinations.REGISTER)

}