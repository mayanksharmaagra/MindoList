package com.jrprofessor.mindolist.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jrprofessor.mindolist.screen.AllTaskScreen
import com.jrprofessor.mindolist.screen.AnalyticsScreen
import com.jrprofessor.mindolist.screen.DashboardScreen
import com.jrprofessor.mindolist.screen.EditProfileScreen
import com.jrprofessor.mindolist.screen.SettingsScreen

//import com.jrprofessor.mindolist.screen.DashboardScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Dashboard.route,
    ) {
        composable(route = BottomNavItem.Dashboard.route) {
            DashboardScreen {
                navController.navigate(BottomNavItem.Tasks.route)
            }
        }
        composable(route = BottomNavItem.Tasks.route) {
            AllTaskScreen()
        }
        composable(route = BottomNavItem.Analytics.route) {
           AnalyticsScreen()
        }
        composable(route = BottomNavItem.Settings.route) {
            SettingsScreen(
                onNavigateToSignUp = onLogout,
                onEditProfileClick = {
                    navController.navigate(Screen.EditProfile.route)
                }
            )
        }
        // ✅ Add EditProfile here inside NavHost
        composable(route = Screen.EditProfile.route) {
            EditProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}