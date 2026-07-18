package com.jrprofessor.mindolist.navGraph

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import com.jrprofessor.mindolist.icons.AnalyticsSelected
import com.jrprofessor.mindolist.icons.AnalyticsUnselected
import com.jrprofessor.mindolist.icons.DashboardSelected
import com.jrprofessor.mindolist.icons.DashboardUnselected
import com.jrprofessor.mindolist.icons.ProfileSelected
import com.jrprofessor.mindolist.icons.ProfileUnselected
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
        label = "Stats",
        selectedIcon = Icons.AnalyticsSelected,
        unSelectedIcon = Icons.AnalyticsUnselected
    )

    object Settings : BottomNavItem(
        route = Screen.Settings.route,
        label = "Settings",
        selectedIcon = SettingsSelected,
        unSelectedIcon = SettingsUnselected
    )
}