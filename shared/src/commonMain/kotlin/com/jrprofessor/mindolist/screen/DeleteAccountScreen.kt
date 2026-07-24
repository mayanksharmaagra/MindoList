package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.theme.*
import com.jrprofessor.mindolist.viewmodels.DeleteAccountViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DeleteAccountScreen(
    viewModel: DeleteAccountViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onAccountDeleted: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) {
            onAccountDeleted()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MindoListTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
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
                    border = BorderStroke(
                        1.dp,
                        MindoListTheme.colors.textSecondary.copy(alpha = 0.1f)
                    )
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
                    text = "Delete Account",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MindoListTheme.colors.textPrimary,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(44.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Warning Icon ──────────────────────────────────────────────
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp),
                color = MindoListTheme.colors.error.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MindoListTheme.colors.error,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Delete your account",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MindoListTheme.colors.textPrimary,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(
                    "This action is ",
                    color = MindoListTheme.colors.textSecondary,
                    fontSize = 16.sp
                )
                Text(
                    "permanent",
                    color = MindoListTheme.colors.error,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    " and cannot be undone.",
                    color = MindoListTheme.colors.textSecondary,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Consequences ──────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ConsequenceItem("All your tasks, categories, and notes will be permanently deleted")
                ConsequenceItem("Your analytics history and streaks will be lost")
                ConsequenceItem("You'll be signed out of MindoList on all devices")
                ConsequenceItem("This cannot be reversed, even by our support team")
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Confirmation Box ──────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MindoListTheme.colors.error.copy(
                        alpha = 0.05f
                    )
                ),
                border = BorderStroke(1.dp, MindoListTheme.colors.error.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "TYPE DELETE TO CONFIRM",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MindoListTheme.colors.error.copy(alpha = 0.5f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = state.confirmationText,
                        onValueChange = viewModel::onConfirmationTextChange,
                        placeholder = {
                            Text(
                                "DELETE",
                                color = MindoListTheme.colors.textSecondary.copy(alpha = 0.3f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MindoListTheme.colors.error.copy(alpha = 0.2f),
                            unfocusedBorderColor = MindoListTheme.colors.error.copy(alpha = 0.1f),
                            focusedContainerColor = MindoListTheme.colors.background,
                            unfocusedContainerColor = MindoListTheme.colors.background,
                            focusedTextColor = MindoListTheme.colors.textPrimary,
                            unfocusedTextColor = MindoListTheme.colors.textPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Agreement Checkbox ────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable { viewModel.onConfirmedChange(!state.isConfirmed) },
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = state.isConfirmed,
                    onCheckedChange = { viewModel.onConfirmedChange(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MindoListTheme.colors.accent,
                        uncheckedColor = MindoListTheme.colors.textSecondary
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "I understand this action is permanent and I will lose all my data.",
                    fontSize = 15.sp,
                    color = MindoListTheme.colors.textSecondary,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Delete Button ─────────────────────────────────────────────
            Button(
                onClick = { viewModel.deleteAccount() },
                enabled = state.isDeleteEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MindoListTheme.colors.cardChildBg,
                    contentColor = MindoListTheme.colors.textPrimary,
                    disabledContainerColor = MindoListTheme.colors.cardChildBg.copy(alpha = 0.5f)
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MindoListTheme.colors.textPrimary
                    )
                } else {
                    Text(
                        "Delete my account",
                        color = if (state.isDeleteEnabled) MindoListTheme.colors.textPrimary else MindoListTheme.colors.textSecondary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row {
                    Text(
                        "Keep my account ",
                        color = MindoListTheme.colors.success,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "and go back",
                        color = MindoListTheme.colors.textSecondary,
                        fontSize = 15.sp,
                        modifier = Modifier.clickable { onBackClick() })
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun ConsequenceItem(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Surface(
            modifier = Modifier.size(20.dp).offset(y = 2.dp),
            shape = CircleShape,
            color = MindoListTheme.colors.error.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MindoListTheme.colors.error,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = MindoListTheme.colors.textPrimary,
            lineHeight = 22.sp
        )
    }
}
