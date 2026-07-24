package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jrprofessor.mindolist.icons.IcAdd
import com.jrprofessor.mindolist.navGraph.BottomNavGraph
import com.jrprofessor.mindolist.navGraph.BottomNavItem
import com.jrprofessor.mindolist.theme.MindoListTheme
import com.jrprofessor.mindolist.theme.MindoListAccentFixed

@Composable
fun HomeScreen(
    onTaskAdd: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination?.route

    val bottomBarRoutes = listOf(
        BottomNavItem.Dashboard.route,
        BottomNavItem.Tasks.route,
        BottomNavItem.Analytics.route,
        BottomNavItem.Settings.route
    )
    val shouldShowBottomBar = currentDestination in bottomBarRoutes

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MindoListTheme.colors.background)
    ) {
        BottomNavGraph(
            navController = navController,
            onAddTaskClick = onTaskAdd,
            onLogout = onLogout
        )
        
        if (shouldShowBottomBar) {
            // FAB - Show on Dashboard and Tasks screens
            if (currentDestination == BottomNavItem.Dashboard.route || currentDestination == BottomNavItem.Tasks.route) {
                FloatingActionButton(
                    onClick = onTaskAdd,
                    containerColor = MindoListAccentFixed,
                    contentColor = Color.Black,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 120.dp, end = 24.dp)
                        .size(64.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = MindoListAccentFixed.copy(alpha = 0.5f)
                        )
                ) {
                    Icon(
                        imageVector = IcAdd,
                        contentDescription = "Add Task",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            MindoListBottomBar(
                navController,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun MindoListBottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val colors = MindoListTheme.colors
    val listScreen = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Tasks,
        BottomNavItem.Analytics,
        BottomNavItem.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(colors.cardBg)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listScreen.forEach { screen ->
                val isSelected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isSelected) screen.selectedIcon else screen.unSelectedIcon,
                        contentDescription = screen.label,
                        tint = if (isSelected) colors.accent else colors.textSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Text(
                        text = screen.label,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) colors.accent else colors.textSecondary
                        )
                    )
                    
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(colors.accent)
                        )
                    }
                }
            }
        }
    }
}
