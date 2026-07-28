package com.jrprofessor.mindolist.navGraph


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.jrprofessor.mindolist.customView.EmailVerifyScreen
import com.jrprofessor.mindolist.screen.AddTaskScreen
import com.jrprofessor.mindolist.screen.ForgotPasswordScreen
import com.jrprofessor.mindolist.screen.HomeScreen
import com.jrprofessor.mindolist.screen.SplashScreen
import com.jrprofessor.mindolist.screen.WelcomeScreen
import com.jrprofessor.mindolist.viewmodels.LoginViewModel
import com.jrprofessor.mindolist.viewmodels.SignUpViewModel
import org.koin.compose.viewmodel.koinViewModel

sealed class Screen(val route: String) {

    // Graph routes
    object AuthGraph : Screen("auth_graph")
    object MainGraph : Screen("main_graph")

    // Auth Screens
//    object Login : Screen("login")
//    object SignUp : Screen("signup")
    object Splash : Screen("splash")
    object VERIFY : Screen("verify")
    object Welcome : Screen("welcome")

    object ForgotPassword : Screen("forgot_password")

    // Main Screens

    object Home : Screen("home")
    object AddTask : Screen("AddTask")
    object Dashboard : Screen("dashboard")
    object Tasks : Screen("tasks")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")

    object Integration : Screen("Integration")
    object EditProfile : Screen("EditProfile")
    object ChangePassword : Screen("ChangePassword")
    object DeleteAccount : Screen("DeleteAccount")

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
            composable(Screen.Welcome.route) { entry ->
                val authBackStackEntry = remember(entry) {
                    navController.getBackStackEntry(Screen.AuthGraph.route)
                }
                val signUpViewModel: SignUpViewModel = koinViewModel(viewModelStoreOwner = authBackStackEntry)
                val loginViewModel: LoginViewModel = koinViewModel(viewModelStoreOwner = authBackStackEntry)

                WelcomeScreen(
                    loginViewModel = loginViewModel,
                    signUpViewModel = signUpViewModel,
                    onNavigateToHome = {
                        navController.navigate(Screen.MainGraph.route) {
                            popUpTo(Screen.AuthGraph.route) { inclusive = true }
                        }
                    }, onNavigateToSignUpStep = {
                        navController.navigate(Screen.VERIFY.route)
                    }, onNavigateToForgot = {
                        navController.navigate(Screen.ForgotPassword.route)
                    })
            }

            composable(Screen.VERIFY.route) { entry ->
                val authBackStackEntry = remember(entry) {
                    navController.getBackStackEntry(Screen.AuthGraph.route)
                }
                val signUpViewModel: SignUpViewModel = koinViewModel(viewModelStoreOwner = authBackStackEntry)
                val state by signUpViewModel.signUpState.collectAsState()

                EmailVerifyScreen(
                    signUpViewModel = signUpViewModel,
                    email = state.email,
                    otp = state.otp,
                    otpError = state.otpError,
                    canResendOtp = state.canResendOtp,
                    resendCountdown = state.resendCountdown,
                    onBackPressed = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate(Screen.MainGraph.route) {
                            popUpTo(Screen.AuthGraph.route) { inclusive = true }
                        }
                    }
                )
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