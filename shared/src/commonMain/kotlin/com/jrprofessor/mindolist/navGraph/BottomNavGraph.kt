package com.jrprofessor.mindolist.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jrprofessor.mindolist.screen.DashboardScreen

//import com.jrprofessor.mindolist.screen.DashboardScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Dashboard.route,
    ) {
        composable(route = BottomNavItem.Dashboard.route) {
            DashboardScreen()
        }
        composable(route = BottomNavItem.Tasks.route) {

        }
        composable(route = BottomNavItem.Analytics.route) {
        }
        composable(route = BottomNavItem.Settings.route) {
        }
    }
}