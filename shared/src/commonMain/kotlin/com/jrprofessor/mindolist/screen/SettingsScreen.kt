package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.jrprofessor.mindolist.theme.PrimaryBlue
import com.jrprofessor.mindolist.theme.backgroundColor

// ── Colors ────────────────────────────────────────────────────────────────────

private val CardBg = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF1E293B)
private val TextSecondary = Color(0xFF94A3B8)
private val DividerColor = Color(0xFFF1F5F9)
private val DangerRed = Color(0xFFEF4444)

// ── Settings Screen ───────────────────────────────────────────────────────────

@Composable
fun SettingsScreen(
    userName: String = "Alex Thompson",
    userEmail: String = "alex.thompson@mindo.app",
    userAvatarUrl: String? = null,
    isDarkMode: Boolean = false,
    isReminderEnabled: Boolean = true,
    onEditProfileClick: () -> Unit = {},
    onAvatarEditClick: () -> Unit = {},
    onDarkModeToggle: (Boolean) -> Unit = {},
    onReminderToggle: (Boolean) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SettingsTopBar(onBackClick = onBackClick)
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
                ProfileSection(
                    userName = userName,
                    userEmail = userEmail,
                    avatarUrl = userAvatarUrl,
                    onEditProfileClick = onEditProfileClick,
                    onAvatarEditClick = onAvatarEditClick,
                )
            }

            // ── Account section ───────────────────────────────────────────────
            item {
                SectionHeader(title = "ACCOUNT")
                SettingsCard {
                    SettingsMenuItem(
                        icon = Icons.Outlined.Person,
                        label = "My Profile",
                        onClick = { onNavigate("profile") },
                    )
                    SettingsDivider()
                    SettingsMenuItem(
                        icon = Icons.Outlined.Notifications,
                        label = "Notifications",
                        onClick = { onNavigate("notifications") },
                    )
                    SettingsDivider()
                    SettingsMenuItem(
                        icon = Icons.Outlined.Shield,
                        label = "Privacy & Security",
                        onClick = { onNavigate("privacy") },
                    )
                }
            }

            // ── Preferences section ───────────────────────────────────────────
            item {
                SectionHeader(title = "PREFERENCES")
                SettingsCard {
                    SettingsToggleItem(
                        icon = Icons.Outlined.DarkMode,
                        label = "Dark Mode",
                        checked = isDarkMode,
                        onCheckedChange = onDarkModeToggle,
                    )
                    SettingsDivider()
                    SettingsToggleItem(
                        icon = Icons.Outlined.Alarm,
                        label = "Reminder Alerts",
                        checked = isReminderEnabled,
                        onCheckedChange = onReminderToggle,
                    )
                    SettingsDivider()
                    SettingsMenuItem(
                        icon = Icons.Outlined.Language,
                        label = "Language",
                        value = "English",
                        onClick = { onNavigate("language") },
                    )
                    SettingsDivider()
                    SettingsMenuItem(
                        icon = Icons.Outlined.GridView,
                        label = "Default View",
                        value = "List",
                        onClick = { onNavigate("default_view") },
                    )
                }
            }

            // ── About section ─────────────────────────────────────────────────
            item {
                SectionHeader(title = "ABOUT")
                SettingsCard {
                    SettingsMenuItem(
                        icon = Icons.Outlined.Star,
                        label = "Rate the App",
                        trailingIcon = Icons.Outlined.OpenInNew,
                        onClick = { onNavigate("rate") },
                    )
                    SettingsDivider()
                    SettingsMenuItem(
                        icon = Icons.Outlined.Feedback,
                        label = "Send Feedback",
                        onClick = { onNavigate("feedback") },
                    )
                    SettingsDivider()
                    SettingsMenuItem(
                        icon = Icons.Outlined.Description,
                        label = "Privacy Policy",
                        onClick = { onNavigate("privacy_policy") },
                    )
                    SettingsDivider()
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
                LogoutButton(onClick = { showLogoutDialog = true })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // ── Logout confirmation dialog ────────────────────────────────────────────
    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                onLogoutClick()
            },
            onDismiss = { showLogoutDialog = false },
        )
    }
}

// ── Top Bar ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(onBackClick: () -> Unit) {
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

@Composable
private fun ProfileSection(
    userName: String,
    userEmail: String,
    avatarUrl: String?,
    onEditProfileClick: () -> Unit,
    onAvatarEditClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Avatar + pencil button
        Box(contentAlignment = Alignment.BottomEnd) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDE9FE))
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                if (avatarUrl != null) {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                    )
                } else {
                    // Initials fallback
                    Text(
                        text = userName.split(" ")
                            .mapNotNull { it.firstOrNull()?.toString() }
                            .take(2)
                            .joinToString(""),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                    )
                }
            }

            // Pencil FAB
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue)
                    .clickable { onAvatarEditClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit avatar",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = userName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = userEmail,
            fontSize = 13.sp,
            color = TextSecondary,
        )

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