package com.aula.unitechc


import androidx.navigation.NavController


object NavigationUtils {
    fun navigateToSecondScreen(navController: NavController) {
        navController.navigate(Screen.Second.route)
    }
}