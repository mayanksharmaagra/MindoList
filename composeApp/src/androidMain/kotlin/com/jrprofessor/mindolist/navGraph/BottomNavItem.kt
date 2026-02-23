package com.jrprofessor.mindolist.navGraph

import com.jrprofessor.mindolist.R

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: Int,
    val unSelectedIcon: Int
) {
    object Dashboard : BottomNavItem(
        route = Screen.Dashboard.route,
        label = "Dashboard",
        selectedIcon = R.drawable.dashboard_selected,
        unSelectedIcon = R.drawable.dashboard_unselected
    )

    object Tasks : BottomNavItem(
        route = Screen.Tasks.route,
        label = "Tasks",
        selectedIcon = R.drawable.tasks_selected,
        unSelectedIcon = R.drawable.tasks_unselected
    )

    object Analytics : BottomNavItem(
        route = Screen.Analytics.route,
        label = "Analytics",
        selectedIcon = R.drawable.analytics_selected,
        unSelectedIcon = R.drawable.analytics_unselected
    )

    object Settings : BottomNavItem(
        route = Screen.Settings.route,
        label = "Settings",
        selectedIcon = R.drawable.settings_selected,
        unSelectedIcon = R.drawable.settings_unselected
    )
}