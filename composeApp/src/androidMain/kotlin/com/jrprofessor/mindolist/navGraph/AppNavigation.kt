package com.jrprofessor.mindolist.navGraph


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.jrprofessor.mindolist.home.AddTaskScreen
import com.jrprofessor.mindolist.home.HomeScreen
import com.jrprofessor.mindolist.welcomeScreen.LoginScreen
import com.jrprofessor.mindolist.welcomeScreen.LoginViewModel
import com.jrprofessor.mindolist.welcomeScreen.SignUpScreen
import com.jrprofessor.mindolist.welcomeScreen.WelcomeScreen

sealed class Screen(val route: String) {

    // Graph routes
    object AuthGraph : Screen("auth_graph")
    object MainGraph : Screen("main_graph")

    // Auth Screens
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Splash : Screen("splash")

    object ForgotPassword : Screen("forgot_password")

    // Main Screens

    object Home : Screen("home")
    object AddTask : Screen("AddTask")
    object Dashboard : Screen("dashboard")
    object Tasks : Screen("tasks")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: LoginViewModel = hiltViewModel()

    val isLoggedIn = viewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn.value)
            Screen.MainGraph.route
        else Screen.AuthGraph.route
    ) {
        navigation(
            startDestination = Screen.Splash.route,
            route = Screen.AuthGraph.route
        ) {
            composable(Screen.Splash.route) {
                WelcomeScreen({
                    navController.navigate(Screen.SignUp.route)
                }, {
                    navController.navigate(Screen.Login.route)
                })
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.MainGraph.route) {
                            popUpTo(Screen.AuthGraph.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        navController.navigate(Screen.Splash.route) {
                            popUpTo(Screen.Splash.route) { inclusive = false }
                        }
                    },
                    onNavigateToForgot = {

                    }
                )
            }

            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.MainGraph.route) {
                            popUpTo(Screen.AuthGraph.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        navController.navigate(Screen.Splash.route) {
                            popUpTo(Screen.Splash.route) { inclusive = false }
                        }
                    }
                )

            }
        }
        composable(Screen.MainGraph.route) {
            HomeScreen(onTaskAdd = {
                navController.navigate(Screen.AddTask.route)
            })
        }
        composable(Screen.AddTask.route) {
            AddTaskScreen()
        }
    }
}