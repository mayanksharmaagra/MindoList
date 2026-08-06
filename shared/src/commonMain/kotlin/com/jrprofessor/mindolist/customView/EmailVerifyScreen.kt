package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.presentation.signup.SignUpButtonState
import com.jrprofessor.mindolist.presentation.signup.SignUpEvent
import com.jrprofessor.mindolist.presentation.signup.SignUpIntent
import com.jrprofessor.mindolist.theme.*
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.LoginViewModel
import com.jrprofessor.mindolist.viewmodels.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EmailVerifyScreen(
    signUpViewModel: SignUpViewModel = koinViewModel(),
    email: String = "",
    otp: String = "",
    otpError: String? = "",
    canResendOtp: Boolean = false,
    resendCountdown: Int = 60,
    onBackPressed: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    otpLength: Int = 5
) {

    val signUpState by signUpViewModel.signUpState.collectAsState()

    LaunchedEffect(Unit) {
        signUpViewModel.signUpEffect.collectLatest { effect ->
            when (effect) {
                SignUpEvent.NavigateBack -> onBackPressed()
                SignUpEvent.NavigateToHome -> onNavigateToHome()
                SignUpEvent.NavigateToVerifyEmail -> { /* Already on this screen */ }
                is SignUpEvent.ShowError -> showToast(
                    effect.error
                )
                is SignUpEvent.ShowToast -> showToast(effect.message)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MindoListTheme.colors.background)
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MindoListTheme.colors.inputBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = MindoListTheme.colors.accent,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Verify it's you",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MindoListTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = buildAnnotatedString {
                append("We've sent a $otpLength-digit code to ")
                withStyle(
                    style = SpanStyle(
                        color = MindoListTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(email)
                }
                append(". Enter it below to continue.")
            },
            fontSize = 16.sp,
            color = MindoListTheme.colors.textSecondary,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        OtpInputField(
            otp = otp,
            otpLength = otpLength,
            otpSentTimestamp = signUpState.otpSentTimestamp,
            onOtpComplete = {
                signUpViewModel.handleEvent(SignUpIntent.OtpChanged(it))
            }
        )

        if (otpError != null) {
            Text(
                text = otpError,
                color = MindoListTheme.colors.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val minutes = resendCountdown / 60
            val seconds = resendCountdown % 60
            val timerText =
                "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"

            Text(
                text = buildAnnotatedString {
                    append("Code expires in ")
                    withStyle(
                        style = SpanStyle(
                            color = MindoListTheme.colors.accent,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(timerText)
                    }
                },
                color = MindoListTheme.colors.textSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Didn't get it? ",
                    color = MindoListTheme.colors.textSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = "Resend code",
                    color = if (canResendOtp) MindoListTheme.colors.accent else MindoListTheme.colors.textSecondary.copy(
                        alpha = 0.5f
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable(enabled = canResendOtp) {
                        signUpViewModel.handleEvent(SignUpIntent.ResendOtpClicked)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.RadioButtonUnchecked, 
                    contentDescription = null,
                    tint = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Auto-fills from SMS when received",
                    color = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        ActionButton(
            text = "Verify & continue",
            isLoading = signUpState.isLoading,
            isEnabled = otp.length == otpLength,
            containerColor = MindoListAccentFixed,
            textColor = Color.Black,
            fontWeight = FontWeight.Bold,
            isIconVisible = true,
            iconTint = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                signUpViewModel.handleEvent(SignUpIntent.VerifyEmailClicked)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Wrong email? ",
                color = MindoListTheme.colors.textSecondary,
                fontSize = 14.sp
            )
            Text(
                text = "Go back and edit",
                color = MindoListTheme.colors.accent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onBackPressed() }
            )
        }
    }
}

@Composable
private fun OtpInputField(
    otp: String,
    otpLength: Int = 5,
    otpSentTimestamp: Long = 0L,
    onOtpComplete: (String) -> Unit
) {
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

    val otpState = remember {
        mutableStateListOf<String>().apply {
            repeat(otpLength) { add("") }
        }
    }

    LaunchedEffect(otp, otpSentTimestamp) {
        if (otp.isEmpty()) {
            repeat(otpLength) { index ->
                otpState[index] = ""
            }
            focusRequesters.first().requestFocus()
        } else if (otp.length == otpLength) {
            // Only sync from external if it's a full OTP to prevent shifting during manual edits
            repeat(otpLength) { index ->
                otpState[index] = otp.getOrNull(index)?.toString() ?: ""
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(otpLength) { index ->
            val isFocused = remember { mutableStateOf(false) }

            OutlinedTextField(
                value = otpState[index],
                onValueChange = { value ->
                    val digits = value.filter { it.isDigit() }
                    
                    if (digits.length >= otpLength) {
                        // Handle full paste
                        val fullOtp = digits.take(otpLength)
                        repeat(otpLength) { i -> otpState[i] = fullOtp[i].toString() }
                        onOtpComplete(fullOtp)
                        focusRequesters.last().requestFocus()
                    } else {
                        // Handle single digit edit or deletion
                        val newChar = digits.lastOrNull()?.toString() ?: ""
                        otpState[index] = newChar
                        onOtpComplete(otpState.joinToString(""))

                        if (newChar.isNotEmpty() && index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        } else if (newChar.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
                    .focusRequester(focusRequesters[index])
                    .onFocusChanged { isFocused.value = it.isFocused }
                    .onKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown && event.key == Key.Backspace && otpState[index].isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                            true
                        } else {
                            false
                        }
                    },
                singleLine = true,
                placeholder = {
                    Text(
                        text = "—",
                        color = MindoListTheme.colors.textSecondary.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MindoListTheme.colors.textPrimary
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MindoListTheme.colors.accent,
                    unfocusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.1f),
                    focusedContainerColor = MindoListTheme.colors.inputBg,
                    unfocusedContainerColor = MindoListTheme.colors.inputBg
                )
            )
        }
    }
}
