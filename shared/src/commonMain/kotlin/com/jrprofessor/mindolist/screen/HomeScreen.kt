package com.jrprofessor.mindolist.screen

// ✨ Required imports
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
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
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.utils.StatusBarDarkMode
import mindolist.shared.generated.resources.Res
import org.jetbrains.compose.resources.painterResource

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

    StatusBarDarkMode()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        BottomNavGraph(
            navController = navController,
            onLogout = onLogout
        )
        if (shouldShowBottomBar) {
            BottomNavigationBarGlassUI(
                navController,
                onTaskAdd,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun BottomNavigationBarGlassUI(
    navController: NavHostController,
    onTaskAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp)
    ) {
        // ✨ Glass UI Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.9f),  // ✅ White glass effect
                            Color.White.copy(alpha = 0.85f)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE8EAED).copy(alpha = 0.8f),  // ✅ Light gray border
                            Color(0xFFE8EAED).copy(alpha = 0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(40.dp)
                )
                .shadow(  // ✅ Subtle shadow for depth
                    elevation = 4.dp,
                    shape = RoundedCornerShape(40.dp),
                    ambientColor = Color.Black.copy(alpha = 0.05f),
                    spotColor = Color.Black.copy(alpha = 0.05f)
                )
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listScreen.forEachIndexed { index, screen ->

                if (index == 2) {
                    Spacer(modifier = Modifier.width(60.dp))
                }

                val isSelected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true

                NavItem(
                    screen = screen,
                    isSelected = isSelected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }

        // ✨ Glass FAB with glow effect
        Box(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-28).dp)
        ) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(72.dp)  // ✅ Slightly bigger glow
                    .align(Alignment.Center)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF3B82F6).copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            radius = 100f
                        ),
                        shape = CircleShape
                    )
            )

            // FAB
            FloatingActionButton(
                onClick = {
                    onTaskAdd()
                },
                containerColor = Color(0xFF3B82F6),
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp
                ),
                modifier = Modifier
                    .size(56.dp)  // ✅ Standard FAB size
                    .align(Alignment.Center)
                    .border(
                        width = 3.dp,  // ✅ Thicker border
                        color = Color.White,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = IcAdd,
                    contentDescription = "Add Task",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun RowScope.NavItem(
    screen: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.6f,
        animationSpec = tween(300),
        label = "alpha"
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(
                indication = ripple(
                    bounded = false,
                    radius = 50.dp,
                    color = Color(0xFF3B82F6).copy(alpha = 0.15f)  // ✅ Blue ripple
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .scale(animatedScale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(38.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF3B82F6).copy(alpha = 0.15f),  // ✅ Lighter glow
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )
            }

            Icon(
                imageVector = if (isSelected) {
                    screen.selectedIcon
                } else {
                    screen.unSelectedIcon
                },
                contentDescription = screen.label,
                tint = if (isSelected) {
                    Color(0xFF3B82F6)  // ✅ Blue when selected
                } else {
                    Color(0xFF64748B)  // ✅ Slate gray when inactive
                },
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = screen.label,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) {
                    Color(0xFF3B82F6)  // ✅ Blue when selected
                } else {
                    Color(0xFF64748B)  // ✅ Slate gray when inactive
                }
            ),
            modifier = Modifier.alpha(animatedAlpha)
        )
    }
}

@Composable
fun BottomNavigationBarSolidBG(navController: NavHostController, modifier: Modifier = Modifier) {
    var selectedPosition by remember {
        mutableIntStateOf(0)
    }
    val listScreen = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Tasks,
        BottomNavItem.Analytics,
        BottomNavItem.Settings
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 12.dp)
    ) {
        /*NavigationBar(
            modifier = Modifier
                .height(72.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(Color(0xFF1A1625))
                .padding(horizontal = 24.dp),
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            listScreen.forEachIndexed { index, screen ->
                if (index == 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any {
                        it.route == screen.route
                    } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = {
                        Text(
                            text = screen.label,
                            style = TextStyle(fontSize = 10.sp),
                        )
                    },
                    icon = {
                        Icon(
                            painter = if (selectedPosition == index) {
                                painterResource(screen.selectedIcon)
                            } else {
                                painterResource(screen.unSelectedIcon)
                            },
                            contentDescription = "Navigation Icon",
                            modifier = Modifier.padding(3.dp),
                        )
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF3B82F6),
                        selectedTextColor = Color(0xFF3B82F6),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }*/
        // ✅ Custom Row - NavigationBar की जगह
        // NavigationBar internally icons को properly center नहीं करता
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFF1A1625))
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically  // ✅ Perfect vertical center
        ) {
            listScreen.forEachIndexed { index, screen ->

                // Space for center FAB
                if (index == 2) {
                    Spacer(modifier = Modifier.width(56.dp))
                }

                val isSelected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                            /** it's new for me, need to check with AI*/
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
                    verticalArrangement = Arrangement.Center  // ✅ Perfect vertical center
                ) {
                    Icon(
                        imageVector = if (isSelected) {
                            screen.selectedIcon
                        } else {
                            screen.unSelectedIcon
                        },
                        contentDescription = screen.label,
                        tint = if (isSelected) Color(0xFF3B82F6) else Color(0xFF94A3B8),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = screen.label,
                        style = TextStyle(
                            fontSize = 11.sp,
                            color = if (isSelected) Color(0xFF3B82F6) else Color(0xFF94A3B8)
                        )
                    )
                }
            }
        }
        // Center Add Button (FAB)
        FloatingActionButton(
            onClick = {

            },
            containerColor = Color(0xFF3B82F6),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-30).dp)
        ) {
            Icon(
                imageVector = IcAdd,
                contentDescription = "Add Task",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}