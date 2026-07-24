package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.icons.SecurityIcon
import com.jrprofessor.mindolist.theme.*
import com.jrprofessor.mindolist.viewmodels.ChangePasswordViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MindoListTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding( 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            // ── Header ────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onBackClick,
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MindoListTheme.colors.cardChildBg,
                    border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = MindoListTheme.colors.textPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Change Password",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MindoListTheme.colors.textPrimary,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(44.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Lock Icon ─────────────────────────────────────────────────
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp),
                color = MindoListTheme.colors.accent.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MindoListTheme.colors.accent,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Update your password",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MindoListTheme.colors.textPrimary,
                style = MaterialTheme.typography.headlineLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Choose a strong password you don't already use elsewhere.",
                fontSize = 16.sp,
                color = MindoListTheme.colors.textSecondary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Current Password ──────────────────────────────────────────
            PasswordInputField(
                label = "Current Password",
                value = state.currentPassword,
                onValueChange = viewModel::onCurrentPasswordChange,
                isVisible = state.isCurrentPasswordVisible,
                onToggleVisibility = viewModel::toggleCurrentPasswordVisibility,
                placeholder = "Enter current password"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── New Password ──────────────────────────────────────────────
            PasswordInputField(
                label = "New Password",
                value = state.newPassword,
                onValueChange = viewModel::onNewPasswordChange,
                isVisible = state.isNewPasswordVisible,
                onToggleVisibility = viewModel::toggleNewPasswordVisibility,
                placeholder = "••••••••••••"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Strength Bar ──────────────────────────────────────────────
            PasswordStrengthBar(strength = state.strength)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (state.strength == 4) "Strong password" else "Weak password",
                color = if (state.strength == 4) MindoListTheme.colors.success else MindoListTheme.colors.error,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Confirm Password ──────────────────────────────────────────
            PasswordInputField(
                label = "Confirm New Password",
                value = state.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                isVisible = state.isConfirmPasswordVisible,
                onToggleVisibility = viewModel::toggleConfirmPasswordVisibility,
                placeholder = "••••••••••••"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Requirements Checklist ────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                RequirementItem(label = "At least 8 characters", isMet = state.hasMinLength)
                RequirementItem(label = "One uppercase letter", isMet = state.hasUppercase)
                RequirementItem(label = "One number", isMet = state.hasNumber)
                RequirementItem(label = "One special character", isMet = state.hasSpecialChar)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Update Button ─────────────────────────────────────────────
            Button(
                onClick = { viewModel.updatePassword() },
                enabled = state.isUpdateEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MindoListAccentFixed,
                    disabledContainerColor = MindoListTheme.colors.cardChildBg
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black)
                } else {
                    Text(
                        "Update password",
                        color = if (state.isUpdateEnabled) Color.Black else MindoListTheme.colors.textSecondary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row {
                    Text("Forgot your current password? ", color = MindoListTheme.colors.textSecondary, fontSize = 14.sp)
                    Text(
                        "Log out & reset instead",
                        color = MindoListTheme.colors.accent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { /* Handle logout & reset */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun PasswordInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    placeholder: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MindoListTheme.colors.textSecondary,
            letterSpacing = 1.sp
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MindoListTheme.colors.textSecondary,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = MindoListTheme.colors.textSecondary
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                unfocusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.1f),
                focusedContainerColor = MindoListTheme.colors.inputBg,
                unfocusedContainerColor = MindoListTheme.colors.inputBg,
                focusedTextColor = MindoListTheme.colors.textPrimary,
                unfocusedTextColor = MindoListTheme.colors.textPrimary
            )
        )
    }
}

@Composable
fun PasswordStrengthBar(strength: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(4) { index ->
            val color = if (index < strength) {
                if (strength == 4) MindoListTheme.colors.success else MindoListTheme.colors.accent
            } else MindoListTheme.colors.cardChildBg
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
fun RequirementItem(label: String, isMet: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(20.dp),
            shape = CircleShape,
            color = if (isMet) MindoListTheme.colors.success.copy(alpha = 0.1f) else Color.Transparent,
            border = if (isMet) null else BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.2f))
        ) {
            if (isMet) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MindoListTheme.colors.success,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            color = if (isMet) MindoListTheme.colors.textPrimary else MindoListTheme.colors.textSecondary
        )
    }
}
