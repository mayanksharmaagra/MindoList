package com.jrprofessor.mindolist.navGraph


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.jrprofessor.mindolist.screen.AddTaskScreen
import com.jrprofessor.mindolist.screen.ForgotPasswordScreen
import com.jrprofessor.mindolist.screen.HomeScreen
import com.jrprofessor.mindolist.screen.LoginScreen
import com.jrprofessor.mindolist.screen.SignUpScreen
import com.jrprofessor.mindolist.screen.SplashScreen
import com.jrprofessor.mindolist.screen.WelcomeScreen
import com.jrprofessor.mindolist.viewmodels.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

sealed class Screen(val route: String) {

    // Graph routes
    object AuthGraph : Screen("auth_graph")
    object MainGraph : Screen("main_graph")

    // Auth Screens
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")

    object ForgotPassword : Screen("forgot_password")

    // Main Screens

    object Home : Screen("home")
    object AddTask : Screen("AddTask")
    object Dashboard : Screen("dashboard")
    object Tasks : Screen("tasks")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")
//    object Profile : Screen("profile")

}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        navigation(
            startDestination = Screen.Welcome.route, route = Screen.AuthGraph.route
        ) {
            composable(Screen.Welcome.route) {
                WelcomeScreen({
                    navController.navigate(Screen.SignUp.route)
                }, {
                    navController.navigate(Screen.Login.route)
                })
            }

            composable(Screen.Login.route) {
                LoginScreen(onNavigateToHome = {
                    navController.navigate(Screen.MainGraph.route) {
                        popUpTo(Screen.AuthGraph.route) { inclusive = true }
                    }
                }, onNavigateBack = {
//                        navController.navigate(Screen.Welcome.route) {
//                            popUpTo(Screen.Welcome.route) { inclusive = false }
//                        }
                    navController.popBackStack()
                }, onNavigateToForgot = {
                    navController.navigate(Screen.ForgotPassword.route)
                })
            }

            composable(Screen.SignUp.route) {
                SignUpScreen(onNavigateToHome = {
                    navController.navigate(Screen.MainGraph.route) {
                        popUpTo(Screen.AuthGraph.route) { inclusive = true }
                    }
                }, onNavigateBack = {
//                        navController.navigate(Screen.Welcome.route) {
//                            popUpTo(Screen.Welcome.route) { inclusive = false }
//                        }
                    navController.popBackStack()
                })
            }
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
        navigation(
            startDestination = Screen.Home.route,  // ← fix
            route = Screen.MainGraph.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onTaskAdd = { navController.navigate(Screen.AddTask.route) },
                    onLogout = {
                        navController.navigate(Screen.AuthGraph.route) {
                            popUpTo(Screen.MainGraph.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.AddTask.route) {
                AddTaskScreen(
                    onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}