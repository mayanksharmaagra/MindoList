package com.jrprofessor.mindolist.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jrprofessor.mindolist.screen.*
import com.jrprofessor.mindolist.viewmodels.TaskViewModel

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    onAddTaskClick: () -> Unit,
    onLogout: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Dashboard.route,
    ) {
        composable(route = BottomNavItem.Dashboard.route) {
            DashboardScreen(
                taskViewModel = taskViewModel,
                onAddTaskClick = onAddTaskClick,
                navigateToAllTasks = {
                    navController.navigate(BottomNavItem.Tasks.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = BottomNavItem.Tasks.route) {
            AllTaskScreen(
                taskViewModel = taskViewModel,
                onAddTaskClick = onAddTaskClick
            )
        }
        composable(route = BottomNavItem.Analytics.route) {
           AnalyticsScreen()
        }
        composable(route = BottomNavItem.Settings.route) {
            SettingsScreen(
                onNavigateToSignUp = onLogout,
                onEditProfileClick = {
                    navController.navigate(Screen.EditProfile.route)
                },
                onIntegrationClick = {
                    navController.navigate(Screen.Integration.route)
                },
                onChangePasswordClick = {
                    navController.navigate(Screen.ChangePassword.route)
                },
                onDeleteAccountClick = {
                    navController.navigate(Screen.DeleteAccount.route)
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
        composable(route = Screen.Integration.route) {
            IntegrationsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.ChangePassword.route) {
            ChangePasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.DeleteAccount.route) {
            DeleteAccountScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onAccountDeleted = onLogout
            )
        }
    }
}