package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.customView.ProfileImageSection
import com.jrprofessor.mindolist.customView.ShowEmailView
import com.jrprofessor.mindolist.customView.ShowNameView
import com.jrprofessor.mindolist.customView.ShowPasswordView
import com.jrprofessor.mindolist.customView.SignUpHeader
import com.jrprofessor.mindolist.customView.WelcomeText
import com.jrprofessor.mindolist.presentation.signup.SignUpButtonState
import com.jrprofessor.mindolist.presentation.signup.SignUpEvent
import com.jrprofessor.mindolist.presentation.signup.SignUpIntent
import com.jrprofessor.mindolist.presentation.signup.SignUpState
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.theme.btnColor
import com.jrprofessor.mindolist.theme.stepColor
import com.jrprofessor.mindolist.utils.Logger
import com.jrprofessor.mindolist.utils.StatusBarDarkMode
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

const val SIGN_UP_TAG="SignUpScreen"
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = koinViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit = {}
) {

    val state by viewModel.signUpState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.signUpEffect.collectLatest { effect ->
            when (effect) {
                SignUpEvent.NavigateBack -> onNavigateBack()
                SignUpEvent.NavigateToHome -> onNavigateToHome()
                is SignUpEvent.ShowError -> showToast(
                    effect.error
                )

                is SignUpEvent.ShowToast -> showToast(effect.message)
            }
        }
    }
    SignUpContent(
        state = state,
        onEvent = viewModel::handleEvent
    )
}

@Composable
fun SignUpContent(state: SignUpState, onEvent: (SignUpIntent) -> Unit) {

    // Derived states
    val stepIndicator = when (state.currentStep) {
        SignUpButtonState.CONTINUE_WITH_EMAIL, SignUpButtonState.CREATE_ACCOUNT -> 1
        SignUpButtonState.VERIFY_EMAIL -> 2
        SignUpButtonState.CREATE_PASSWORD -> 3
    }

    val btnTitle = when (state.currentStep) {
        SignUpButtonState.CONTINUE_WITH_EMAIL -> "Continue with email"
        SignUpButtonState.CREATE_ACCOUNT -> "Create an account"
        SignUpButtonState.VERIFY_EMAIL -> "Verify email"
        SignUpButtonState.CREATE_PASSWORD -> "Continue"
    }
    val toolbarTitle = when (state.currentStep) {
        SignUpButtonState.CONTINUE_WITH_EMAIL -> "Create new account"
        SignUpButtonState.CREATE_ACCOUNT -> "Add your email 1/3"
        SignUpButtonState.VERIFY_EMAIL -> "Verify your email 2/3"
        SignUpButtonState.CREATE_PASSWORD -> "Create your password 3/3"
    }

    // Button enable state based on current screen
    val isEnabled = when (state.currentStep) {
        SignUpButtonState.CONTINUE_WITH_EMAIL -> true
        SignUpButtonState.CREATE_ACCOUNT -> state.isEmailValid
        SignUpButtonState.VERIFY_EMAIL -> state.isOtpValid
        SignUpButtonState.CREATE_PASSWORD -> state.isPasswordValid
    }

    StatusBarDarkMode()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        SignUpHeader(toolbarTitle = toolbarTitle, onBackClick = {
            onEvent(SignUpIntent.BackPressed)
        })

        Spacer(modifier = Modifier.height(30.dp))

        // Subtitle or Step Indicator
        if (state.currentStep == SignUpButtonState.CONTINUE_WITH_EMAIL) {
            WelcomeText("Begin with creating new free account. This helps you keep your learning way easier.")
        } else {
            StepIndicator(currentStep = stepIndicator)
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Content based on state
        when (state.currentStep) {
            SignUpButtonState.CREATE_ACCOUNT -> {
                ShowNameView(
                    name = state.name,
                    nameError = state.nameError,
                    onNameChange = {
                        onEvent(SignUpIntent.NameChanged(it))
                    }
                )
                Spacer(modifier = Modifier.height(15.dp))
                ShowEmailView(
                    email = state.email,
                    emailError = state.emailError,
                    onEmailChange = {
                        onEvent(SignUpIntent.EmailChanged(it))
                    }
                )
            }

            SignUpButtonState.VERIFY_EMAIL -> {
                ShowEmailOtpView(
                    emailMsg = "We've sent a 5-digit verification code to ${state.email}. Please check your inbox and enter the code below.",
                    otp = state.otp,
                    otpError = state.otpError,
                    canResendOtp = state.canResendOtp,
                    resendCountdown = state.resendCountdown,
                    onOtpChange = { onEvent(SignUpIntent.OtpChanged(it)) },
                    onResendClick = { onEvent(SignUpIntent.ResendOtpClicked) }
                )
            }

            SignUpButtonState.CREATE_PASSWORD -> {
                Logger.debug {
                    "user profile ${state.profileUrl}"
                }
                ProfileImageSection(
                    userName = state.name,
                    avatarUrl = state.profileUrl,
                    onAvatarEditClick = {
                        onEvent(SignUpIntent.UserProfileUrl(it,state.email))
                    },
                )

                Spacer(modifier = Modifier.height(20.dp))

                ShowPasswordView(
                    password = state.password,
                    passwordError = state.passwordError,
                    onPasswordChange = {
                        onEvent(SignUpIntent.PasswordChanged(it))
                    }
                )
            }

            else -> Unit
        }
        Spacer(modifier = Modifier.height(30.dp))

        ActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            text = btnTitle,
            fontSize = 18.sp,
            textColor = Color.White,
            containerColor = btnColor,
            isIconVisible = false,
            isEnabled = isEnabled && !state.isLoading,
            isLoading = state.isLoading
        ) {
            when (state.currentStep) {
                SignUpButtonState.CONTINUE_WITH_EMAIL ->
                    onEvent(SignUpIntent.ContinueWithEmailClicked)

                SignUpButtonState.CREATE_ACCOUNT ->
                    onEvent(SignUpIntent.CreateAccountClicked)

                SignUpButtonState.VERIFY_EMAIL ->
                    onEvent(SignUpIntent.VerifyEmailClicked)

                SignUpButtonState.CREATE_PASSWORD ->
                    onEvent(SignUpIntent.CreatePasswordClicked)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// =====================================================
// STEP INDICATOR
// =====================================================

@Composable
fun StepIndicator(
    totalSteps: Int = 3,
    currentStep: Int = 1, // 1-based index
    activeColor: Color = stepColor,
    inactiveColor: Color = Color(0xFFE5E7EB)
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSteps) { index ->
            val isActive = index < currentStep // 🔥 key change

            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (isActive) activeColor else inactiveColor
                    )
            )

            if (index != totalSteps - 1) {
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

// =====================================================
// OTP VIEW
// =====================================================

@Composable
fun ShowEmailOtpView(
    emailMsg: String,
    otp: String,
    otpError: String?,
    canResendOtp: Boolean,
    resendCountdown: Int,
    onOtpChange: (String) -> Unit,
    onResendClick: () -> Unit,
    otpLength: Int = 5
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = emailMsg,
            fontSize = 16.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        InputLabel(text = "Code")

        Spacer(modifier = Modifier.height(12.dp))

        OtpInputField(
            otp = otp,
            otpLength = otpLength,
            onOtpComplete = onOtpChange
        )
        if (otpError != null) {
            Text(
                text = otpError,
                color = Color(0xFFDC2626),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Resend OTP button
        TextButton(
            onClick = onResendClick,
            enabled = canResendOtp
        ) {
            Text(
                text = if (canResendOtp) {
                    "Resend code"
                } else {
                    "Resend code in ${resendCountdown}s"
                },
                color = if (canResendOtp) Color(0xFF6366F1) else Color(0xFF9CA3AF),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun OtpInputField(
    otp: String,
    otpLength: Int = 5,
    onOtpComplete: (String) -> Unit
) {
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

    // ✅ CRITICAL FIX: Use individual state for each field
    val otpState = remember {
        mutableStateListOf<String>().apply {
            repeat(otpLength) { add("") }
        }
    }

    // ✅ Sync state when otp prop changes from parent
    LaunchedEffect(otp) {
        Logger.debug { "OTP changed from parent: '$otp'"}
        repeat(otpLength) { index ->
            otpState[index] = otp.getOrNull(index)?.toString() ?: ""
        }
    }


    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }


    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(otpLength) { index ->
            OutlinedTextField(
                value = otpState[index],
                onValueChange = { value ->
                    // ✅ Only allow single digit input
                    if (value.length <= 1 && (value.isEmpty() || value.all { it.isDigit() })) {
                        // ✅ Update local state immediately (this makes it visible)
                        otpState[index] = value

                        // ✅ Build and send complete OTP to parent
                        val newOtp = otpState.joinToString("")
                        Logger.debug { "OtpInputField: $newOtp"}
                        onOtpComplete(newOtp)

                        // ✅ Move to next field if digit entered
                        if (value.isNotEmpty() && index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }

                        // ✅ Move to previous field on backspace
                        if (value.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .focusRequester(focusRequesters[index]),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6366F1),
                    unfocusedBorderColor = Color(0xFFD1D5DB),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        }
    }
}
// =====================================================
// HELPER COMPONENTS
// =====================================================

@Composable
fun InputLabel(text: String,textColor: Color = Color.Black,fontWeight: FontWeight= FontWeight.Normal) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = textColor,
        modifier = Modifier.fillMaxWidth(),
        fontWeight=fontWeight,
        textAlign = TextAlign.Start
    )
}