package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jrprofessor.mindolist.customView.ProfileImageSection
import com.jrprofessor.mindolist.presentation.SettingsAction
import com.jrprofessor.mindolist.presentation.SettingsEvent
import com.jrprofessor.mindolist.theme.PrimaryBlue
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.viewmodels.DashboardViewModel
import com.jrprofessor.mindolist.viewmodels.SettingsViewmodel
import org.koin.compose.viewmodel.koinViewModel

// ── Colors ────────────────────────────────────────────────────────────────────

private val CardBg = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF1E293B)
private val TextSecondary = Color(0xFF94A3B8)
private val DividerColor = Color(0xFFF1F5F9)
private val DangerRed = Color(0xFFEF4444)

// ── Settings Screen ───────────────────────────────────────────────────────────

@Composable
fun SettingsScreen(
    viewModelDashboard: DashboardViewModel = koinViewModel(),
    viewModelSettings: SettingsViewmodel = koinViewModel(),
    isReminderEnabled: Boolean = true,
    onAvatarEditClick: (ByteArray) -> Unit = {},
    onReminderToggle: (Boolean) -> Unit = {},
    onNavigateToSignUp: () -> Unit,
) {
    val stateDashboard by viewModelDashboard.state.collectAsState()
    val stateSettings by viewModelSettings.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    // Collect one-time events
    LaunchedEffect(Unit) {
        viewModelSettings.event.collect { event ->
            when (event) {
                is SettingsEvent.Message -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is SettingsEvent.NavigateToSignUp -> {
                    onNavigateToSignUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            SettingsTopBar()
        },
        containerColor = backgroundColor,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {

            // ── Profile section ───────────────────────────────────────────────
            item {
                ProfileImageSection(
                    userName = stateDashboard.user?.displayName,
                    avatarUrl = stateDashboard.user?.profileUrl,
                    onAvatarEditClick = onAvatarEditClick,
                    isEdit=false
                )
            }
            // ------- Profile name section -----------
            item {
                ProfileDetailsSection(
                    userName = stateDashboard.user?.displayName,
                    userEmail = stateDashboard.user?.email,
                    onEditProfileClick = { viewModelSettings.dispatch(SettingsAction.EditProfile) },
                )
            }

            // ── Account section ───────────────────────────────────────────────
            item {
                SectionHeader(title = "ACCOUNT")
                SettingsCard {
                    SettingsMenuItem(
                        icon = Icons.Outlined.Person,
                        label = "My Profile",
                        onClick = { viewModelSettings.dispatch(SettingsAction.ShowProfile) },
                    )
//                    SettingsDivider()
//                    SettingsMenuItem(
//                        icon = Icons.Outlined.Notifications,
//                        label = "Notifications",
//                        onClick = { onNavigate("notifications") },
//                    )
//                    SettingsDivider()
//                    SettingsMenuItem(
//                        icon = Icons.Outlined.Shield,
//                        label = "Privacy & Security",
//                        onClick = { onNavigate("privacy") },
//                    )
                }
            }

            // ── Preferences section ───────────────────────────────────────────
            item {
                SectionHeader(title = "PREFERENCES")
                SettingsCard {
//                    SettingsToggleItem(
//                        icon = Icons.Outlined.DarkMode,
//                        label = "Dark Mode",
//                        checked = isDarkMode,
//                        onCheckedChange = onDarkModeToggle,
//                    )
//                    SettingsDivider()
                    SettingsToggleItem(
                        icon = Icons.Outlined.Alarm,
                        label = "Reminder Alerts",
                        checked = isReminderEnabled,
                        onCheckedChange = onReminderToggle,
                    )
//                    SettingsDivider()
//                    SettingsMenuItem(
//                        icon = Icons.Outlined.Language,
//                        label = "Language",
//                        value = "English",
//                        onClick = { onNavigate("language") },
//                    )
//                    SettingsDivider()
//                    SettingsMenuItem(
//                        icon = Icons.Outlined.GridView,
//                        label = "Default View",
//                        value = "List",
//                        onClick = { onNavigate("default_view") },
//                    )
                }
            }

            // ── About section ─────────────────────────────────────────────────
            item {
                SectionHeader(title = "ABOUT")
                SettingsCard {
//                    SettingsMenuItem(
//                        icon = Icons.Outlined.Star,
//                        label = "Rate the App",
//                        trailingIcon = Icons.Outlined.OpenInNew,
//                        onClick = { onNavigate("rate") },
//                    )
//                    SettingsDivider()
//                    SettingsMenuItem(
//                        icon = Icons.Outlined.Feedback,
//                        label = "Send Feedback",
//                        onClick = { onNavigate("feedback") },
//                    )
//                    SettingsDivider()
//                    SettingsMenuItem(
//                        icon = Icons.Outlined.Description,
//                        label = "Privacy Policy",
//                        onClick = { onNavigate("privacy_policy") },
//                    )
//                    SettingsDivider()
                    SettingsMenuItem(
                        icon = Icons.Outlined.Info,
                        label = "App Version",
                        value = "1.0.0",
                        showArrow = false,
                        onClick = {},
                    )
                }
            }

            // ── Logout button ─────────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LogoutButton(onClick = { viewModelSettings.dispatch(SettingsAction.Logout)})
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // ── Logout confirmation dialog ────────────────────────────────────────────
    if (stateSettings.showLogoutConfirmDialog) {
        LogoutDialog(
            onConfirm = {
                viewModelSettings.confirmLogout()
            },
            onDismiss = { viewModelSettings.dismissLogoutDialog() },
        )
    }
}

@Composable
fun ProfileDetailsSection(
    userName: String?,
    userEmail: String?,
    onEditProfileClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        userName?.let {
            Text(
                text = it,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        userEmail?.let {
            Text(
                text = it,
                fontSize = 13.sp,
                color = TextSecondary,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Edit Profile button
        Button(
            onClick = onEditProfileClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
                .height(44.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
            ),
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
        }
    }
}

// ── Top Bar ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(onBackClick: () -> Unit={}) {
    TopAppBar(
        title = {
            Text(
                text = "Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
            )
        },
        navigationIcon = {
//            IconButton(onClick = onBackClick) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = "Back",
//                    tint = PrimaryBlue,
//                )
//            }
        },
        actions = {
//            IconButton(onClick = {}) {
//                Icon(
//                    imageVector = Icons.Default.MoreVert,
//                    contentDescription = "More",
//                    tint = PrimaryBlue,
//                )
//            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
        ),
    )
}

// ── Profile Section ───────────────────────────────────────────────────────────


// ── Section Header ────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextSecondary,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = 20.dp,
            bottom = 8.dp,
        ),
    )
}

// ── Settings Card ─────────────────────────────────────────────────────────────

@Composable
private fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(content = content)
    }
}

// ── Menu Item ─────────────────────────────────────────────────────────────────

@Composable
private fun SettingsMenuItem(
    icon: ImageVector,
    label: String,
    value: String? = null,
    showArrow: Boolean = true,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(22.dp),
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
        )
        if (value != null) {
            Text(
                text = value,
                fontSize = 13.sp,
                color = TextSecondary,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp),
            )
        } else if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

// ── Toggle Item ───────────────────────────────────────────────────────────────

@Composable
private fun SettingsToggleItem(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(22.dp),
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryBlue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFCBD5E1),
            ),
        )
    }
}

// ── Divider ───────────────────────────────────────────────────────────────────

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        thickness = 0.5.dp,
        color = DividerColor,
    )
}

// ── Logout Button ─────────────────────────────────────────────────────────────

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(52.dp),
        shape = RoundedCornerShape(50.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, DangerRed),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = DangerRed,
        ),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Logout,
            contentDescription = null,
            tint = DangerRed,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Logout",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = DangerRed,
        )
    }
}

// ── Logout Dialog ─────────────────────────────────────────────────────────────

@Composable
private fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Log Out",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Are you sure you want to log out?",
                    fontSize = 14.sp,
                    color = TextSecondary,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(50.dp),
                    ) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DangerRed
                        ),
                    ) {
                        Text("Log Out", color = Color.White)
                    }
                }
            }
        }
    }
}

// ── Usage ─────────────────────────────────────────────────────────────────────
//
// SettingsScreen(
//     userName = state.userName,
//     userEmail = state.userEmail,
//     userAvatarUrl = state.avatarUrl,
//     isDarkMode = state.isDarkMode,
//     isReminderEnabled = state.isReminderEnabled,
//     onEditProfileClick = { navController.navigate("edit_profile") },
//     onAvatarEditClick = { /* image picker */ },
//     onDarkModeToggle = { viewModel.dispatch(SettingsAction.DarkModeToggle(it)) },
//     onReminderToggle = { viewModel.dispatch(SettingsAction.ReminderToggle(it)) },
//     onLogoutClick = { viewModel.dispatch(SettingsAction.Logout) },
//     onBackClick = { navController.popBackStack() },
//     onNavigate = { dest -> navController.navigate(dest) },
// )