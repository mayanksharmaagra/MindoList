package com.jrprofessor.mindolist.navGraph

import androidx.compose.ui.graphics.vector.ImageVector
import com.jrprofessor.mindolist.icons.AnalyticsSelectedIcon
import com.jrprofessor.mindolist.icons.AnalyticsUnSelectedIcon
import com.jrprofessor.mindolist.icons.DashboardSelected
import com.jrprofessor.mindolist.icons.DashboardUnselected
import com.jrprofessor.mindolist.icons.SettingsSelected
import com.jrprofessor.mindolist.icons.SettingsUnselected
import com.jrprofessor.mindolist.icons.TasksSelected
import com.jrprofessor.mindolist.icons.TasksUnselected


sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
) {
    object Dashboard : BottomNavItem(
        route = Screen.Dashboard.route,
        label = "Dashboard",
        selectedIcon = DashboardSelected,
        unSelectedIcon = DashboardUnselected
    )

    object Tasks : BottomNavItem(
        route = Screen.Tasks.route,
        label = "Tasks",
        selectedIcon = TasksSelected,
        unSelectedIcon = TasksUnselected
    )

    object Analytics : BottomNavItem(
        route = Screen.Analytics.route,
        label = "Analytics",
        selectedIcon = AnalyticsSelectedIcon,
        unSelectedIcon = AnalyticsUnSelectedIcon
    )

    object Settings : BottomNavItem(
        route = Screen.Settings.route,
        label = "Settings",
        selectedIcon = SettingsSelected,
        unSelectedIcon = SettingsUnselected
    )
}