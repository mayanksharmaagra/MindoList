package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.ShowEmailView
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordEvent
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordIntent
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordState
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordStep
import com.jrprofessor.mindolist.theme.*
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.ForgotPasswordViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.forgotEffect.collectLatest { effect ->
            when (effect) {
                ForgotPasswordEvent.NavigateBack -> onNavigateBack()
                is ForgotPasswordEvent.ShowError -> showToast(
                    effect.error
                )

                is ForgotPasswordEvent.ShowToast -> showToast(effect.message)
            }
        }
    }

    ForgotPasswordContent(
        state = state,
        onEmailChange = {
            viewModel.forgotPasswordEventHandle(ForgotPasswordIntent.EmailChanged(it))
        },
        onContinueClicked = {
            viewModel.forgotPasswordEventHandle(ForgotPasswordIntent.ContinueWithEmailClicked)
        },
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun ForgotPasswordContent(
    state: ForgotPasswordState,
    onEmailChange: (String) -> Unit,
    onContinueClicked: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val colors = MindoListTheme.colors
    val isEnabled = when (state.currentStep) {
        ForgotPasswordStep.ENTER_EMAIL -> state.isEmailValid
        else -> true
    }
    val ledgerGradient = Brush.linearGradient(
        colors = listOf(colors.accent, MindoListSecond)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Lock Icon at the top
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.inputBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (state.currentStep == ForgotPasswordStep.ENTER_EMAIL) {
            Text(
                text = "Reset your password",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Enter the email linked to your account and we'll send a reset link.",
                fontSize = 16.sp,
                color = colors.textSecondary,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            ShowEmailView(
                email = state.email,
                emailError = state.emailError,
                label = "EMAIL",
                labelColor = colors.textSecondary,
                textColor = colors.textPrimary,
                containerColor = colors.inputBg,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = colors.accent,
                onEmailChange = onEmailChange
            )
        }

        if (state.currentStep == ForgotPasswordStep.EMAIL_SENT) {
            Text(
                text = "Check your email",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "We have sent an email to ${state.email} with instructions to reset your password.",
                fontSize = 16.sp,
                color = colors.textSecondary,
                lineHeight = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                when (state.currentStep) {
                    ForgotPasswordStep.ENTER_EMAIL -> onContinueClicked()
                    else -> onNavigateBack()
                }
            },
            enabled = isEnabled && !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Black.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isEnabled) ledgerGradient else Brush.linearGradient(listOf(Color.Gray, Color.DarkGray))),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = if (state.currentStep == ForgotPasswordStep.ENTER_EMAIL) "Send reset link" else "Back to Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Remembered your password? ",
                color = colors.textSecondary,
                fontSize = 14.sp
            )
            Text(
                text = "Log in",
                color = colors.accent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateBack() }
            )
        }
    }
}
