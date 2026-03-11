package com.jrprofessor.mindolist.navGraph


sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: Int,
    val unSelectedIcon: Int
) {
    object Dashboard : BottomNavItem(
        route = Screen.Dashboard.route,
        label = "Dashboard",
        selectedIcon = Res.drawable.dashboard_selected,
        unSelectedIcon = Res.drawable.dashboard_unselected
    )

    object Tasks : BottomNavItem(
        route = Screen.Tasks.route,
        label = "Tasks",
        selectedIcon = Res.drawable.tasks_selected,
        unSelectedIcon = Res.drawable.tasks_unselected
    )

    object Analytics : BottomNavItem(
        route = Screen.Analytics.route,
        label = "Analytics",
        selectedIcon = Res.drawable.analytics_selected,
        unSelectedIcon = Res.drawable.analytics_unselected
    )

    object Settings : BottomNavItem(
        route = Screen.Settings.route,
        label = "Settings",
        selectedIcon = Res.drawable.settings_selected,
        unSelectedIcon = Res.drawable.settings_unselected
    )
}