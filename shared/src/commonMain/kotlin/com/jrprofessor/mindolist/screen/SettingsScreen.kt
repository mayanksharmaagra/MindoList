package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.icons.GoogleLogo
import com.jrprofessor.mindolist.presentation.dashboard.DashboardAction
import com.jrprofessor.mindolist.presentation.settings.SettingsAction
import com.jrprofessor.mindolist.presentation.settings.SettingsEvent
import com.jrprofessor.mindolist.theme.*
import com.jrprofessor.mindolist.theme.MindoListTheme
import com.jrprofessor.mindolist.viewmodels.DashboardViewModel
import com.jrprofessor.mindolist.viewmodels.SettingsViewmodel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    viewModelDashboard: DashboardViewModel = koinViewModel(),
    viewModelSettings: SettingsViewmodel = koinViewModel(),
    onNavigateToSignUp: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onIntegrationClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
) {
    val stateDashboard by viewModelDashboard.state.collectAsState()
    val stateSettings by viewModelSettings.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showThemeDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModelDashboard.dispatch(DashboardAction.LoadUserData)
        viewModelSettings.event.collect { event ->
            when (event) {
                is SettingsEvent.Message -> snackbarHostState.showSnackbar(event.message)
                is SettingsEvent.NavigateToSignUp -> onNavigateToSignUp()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MindoListTheme.colors.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Settings",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MindoListTheme.colors.textPrimary,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            // ── Profile Card ───────────────────────────────────────────────
            item {
                ProfileCard(
                    userName = stateDashboard.user?.displayName ?: "User Name",
                    userEmail = stateDashboard.user?.email ?: "user@email.com",
                    onClick = onEditProfileClick
                )
            }
            // ── CONNECTIONS ────────────────────────────────────────────────
            item {
                SettingsSection(title = "CONNECTIONS") {
                    SettingsMenuItem(
                        icon = GoogleLogo,
                        label = "Integrations",
                        value = if (stateDashboard.user?.isGoogleConnected == true) "Connected" else "Not Connected",
                        iconTint = GoogleColor,
                    ) {
                        onIntegrationClick()
                    }
                }
            }

            // ── Preferences ────────────────────────────────────────────────
            item {
                SettingsSection(title = "PREFERENCES") {
                    SettingsMenuItem(
                        icon = if (stateSettings.themeMode == ThemeMode.LIGHT) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                        label = "Theme",
                        value = stateSettings.themeMode.name.lowercase().replaceFirstChar { it.uppercase() }
                    ) {
                        showThemeDialog = true
                    }
                    SettingsDivider()
                    SettingsToggleItem(
                        icon = Icons.Outlined.Notifications,
                        label = "Notifications",
                        checked = stateSettings.notificationsEnabled,
                        onCheckedChange = {
                            viewModelSettings.dispatch(SettingsAction.SetNotificationsEnabled(it))
                        }
                    )
                    SettingsDivider()
                    SettingsMenuItem(
                        icon = Icons.Outlined.GridView,
                        label = "Default view",
                        value = "Grouped"
                    ) {}
                    SettingsDivider()
                    SettingsToggleItem(
                        icon = Icons.Default.AutoAwesome,
                        label = "AI extraction",
                        checked = stateSettings.aiExtractionEnabled,
                        onCheckedChange = {
                            viewModelSettings.dispatch(SettingsAction.SetAiExtractionEnabled(it))
                        }
                    )
                }
            }

            // ── Account ───────────────────────────────────────────────────
            item {
                SettingsSection(title = "ACCOUNT") {
                    SettingsMenuItem(
                        icon = Icons.Outlined.Lock,
                        label = "Change password"
                    ) { onChangePasswordClick() }
                    SettingsDivider()
                    SettingsMenuItem(
                        icon = Icons.Outlined.Delete,
                        label = "Delete account",
                        labelColor = MindoListTheme.colors.error,
                        iconTint = MindoListTheme.colors.error.copy(alpha = 0.5f),
                        showArrow = false
                    ) { onDeleteAccountClick() }
                    SettingsDivider()
                    SettingsMenuItem(
                        icon = Icons.AutoMirrored.Outlined.Logout,
                        label = "Log out",
                        showArrow = false
                    ) { viewModelSettings.dispatch(SettingsAction.Logout) }
                }
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "MindoList v1.0.0",
                        color = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    if (stateSettings.showLogoutConfirmDialog) {
        LogoutDialog(
            onConfirm = { viewModelSettings.confirmLogout() },
            onDismiss = { viewModelSettings.dismissLogoutDialog() }
        )
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentMode = stateSettings.themeMode,
            onModeSelected = {
                viewModelSettings.dispatch(SettingsAction.SetThemeMode(it))
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
}

@Composable
fun ProfileCard(
    userName: String,
    userEmail: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg),
        border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f)),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp),
                color = MindoListAccentFixed
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = userName.firstOrNull()?.toString()?.uppercase() ?: "A",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MindoListTheme.colors.textPrimary
                )
                Text(
                    text = userEmail,
                    fontSize = 14.sp,
                    color = MindoListTheme.colors.textSecondary
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MindoListTheme.colors.textSecondary,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg),
            border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsMenuItem(
    icon: ImageVector,
    label: String,
    value: String? = null,
    labelColor: Color = MindoListTheme.colors.textPrimary,
    iconTint: Color = MindoListTheme.colors.textSecondary,
    showArrow: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onClick() }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = labelColor,
            modifier = Modifier.weight(1f)
        )
        if (value != null) {
            Text(
                text = value,
                fontSize = 15.sp,
                color = MindoListTheme.colors.textSecondary
            )
        } else if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MindoListTheme.colors.textSecondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MindoListTheme.colors.textPrimary,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MindoListAccentFixed,
                uncheckedThumbColor = MindoListTheme.colors.textSecondary,
                uncheckedTrackColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.1f)
            )
        )
    }
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 20.dp),
        thickness = 0.5.dp,
        color = MindoListTheme.colors.textSecondary.copy(alpha = 0.1f)
    )
}

@Composable
fun LogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg),
            border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Log out",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MindoListTheme.colors.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Are you sure you want to log out from MindoList?",
                    fontSize = 14.sp,
                    color = MindoListTheme.colors.textSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        containerColor = MindoListTheme.colors.cardChildBg,
                        textColor = MindoListTheme.colors.textPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        elevation = 0.dp
                    )
                    ActionButton(
                        text = "Log out",
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        containerColor = MindoListTheme.colors.error,
                        textColor = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        elevation = 0.dp
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeSelectionDialog(
    currentMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg),
            border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Choose Theme",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MindoListTheme.colors.textPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                ThemeOptionItem("System Default", ThemeMode.SYSTEM, currentMode == ThemeMode.SYSTEM) { onModeSelected(ThemeMode.SYSTEM) }
                ThemeOptionItem("Light", ThemeMode.LIGHT, currentMode == ThemeMode.LIGHT) { onModeSelected(ThemeMode.LIGHT) }
                ThemeOptionItem("Dark", ThemeMode.DARK, currentMode == ThemeMode.DARK) { onModeSelected(ThemeMode.DARK) }
            }
        }
    }
}

@Composable
fun ThemeOptionItem(
    label: String,
    mode: ThemeMode,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) MindoListTheme.colors.accent.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MindoListTheme.colors.accent,
                unselectedColor = MindoListTheme.colors.textSecondary
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = if (isSelected) MindoListTheme.colors.accent else MindoListTheme.colors.textPrimary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
