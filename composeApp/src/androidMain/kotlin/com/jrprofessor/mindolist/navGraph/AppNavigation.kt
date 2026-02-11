package com.jrprofessor.mindolist.navGraph


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jrprofessor.mindolist.presentation.SignUpState
import com.jrprofessor.mindolist.welcomeScreen.SignUpScreen
import com.jrprofessor.mindolist.welcomeScreen.SignUpViewModel
import com.jrprofessor.mindolist.welcomeScreen.WelcomeScreen

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Splash : Screen("splash")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: SignUpViewModel = hiltViewModel()

    val startDestination = Screen.Splash.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {

            WelcomeScreen({
                navController.navigate(Screen.SignUp.route)
            }, {
                navController.navigate(Screen.Login.route)
            })
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToHome = {

                },
                onNavigateBack = {

                }
            )
        }
    }
}