package com.jrprofessor.mindolist.welcomeScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jrprofessor.mindolist.R
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.presentation.EmailButtonState
import com.jrprofessor.mindolist.presentation.SignUpEvent
import com.jrprofessor.mindolist.presentation.SignUpIntent
import com.jrprofessor.mindolist.presentation.SignUpState
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.theme.btnColor
import com.jrprofessor.mindolist.theme.stepColor
import com.jrprofessor.mindolist.utils.StatusBarInDarkMode
import kotlinx.coroutines.flow.collectLatest

const val TAG="SignUpScreen"
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit = {}
) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SignUpEvent.NavigateBack -> onNavigateBack()
                SignUpEvent.NavigateToHome -> onNavigateToHome()
                is SignUpEvent.ShowError -> Toast.makeText(
                    context,
                    effect.error,
                    Toast.LENGTH_SHORT
                ).show()

                is SignUpEvent.ShowToast -> Toast.makeText(
                    context,
                    effect.message,
                    Toast.LENGTH_LONG
                ).show()
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
        EmailButtonState.CONTINUE_WITH_EMAIL, EmailButtonState.CREATE_ACCOUNT -> 1
        EmailButtonState.VERIFY_EMAIL -> 2
        EmailButtonState.CREATE_PASSWORD -> 3
    }

    val btnTitle = when (state.currentStep) {
        EmailButtonState.CONTINUE_WITH_EMAIL -> "Continue with email"
        EmailButtonState.CREATE_ACCOUNT -> "Create an account"
        EmailButtonState.VERIFY_EMAIL -> "Verify email"
        EmailButtonState.CREATE_PASSWORD -> "Continue"
    }
    val toolbarTitle = when (state.currentStep) {
        EmailButtonState.CONTINUE_WITH_EMAIL -> "Create new account"
        EmailButtonState.CREATE_ACCOUNT -> "Add your email 1/3"
        EmailButtonState.VERIFY_EMAIL -> "Verify your email 2/3"
        EmailButtonState.CREATE_PASSWORD -> "Create your password 3/3"
    }

    // Button enable state based on current screen
    val isEnabled = when (state.currentStep) {
        EmailButtonState.CONTINUE_WITH_EMAIL -> true
        EmailButtonState.CREATE_ACCOUNT -> state.isEmailValid
        EmailButtonState.VERIFY_EMAIL -> state.isOtpValid
        EmailButtonState.CREATE_PASSWORD -> state.isPasswordValid
    }

    StatusBarInDarkMode()

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
        if (state.currentStep == EmailButtonState.CONTINUE_WITH_EMAIL) {
            WelcomeText()
        } else {
            StepIndicator(currentStep = stepIndicator)
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Content based on state
        when (state.currentStep) {
            EmailButtonState.CREATE_ACCOUNT -> {
                ShowEmailView(
                    email = state.email,
                    emailError = state.emailError,
                    onEmailChange = {
                        onEvent(SignUpIntent.EmailChanged(it))
                    }
                )
            }

            EmailButtonState.VERIFY_EMAIL -> {
                ShowEmailOtpView(
                    email = state.email,
                    otp = state.otp,
                    otpError = state.otpError,
                    canResendOtp = state.canResendOtp,
                    resendCountdown = state.resendCountdown,
                    onOtpChange = { onEvent(SignUpIntent.OtpChanged(it)) },
                    onResendClick = { onEvent(SignUpIntent.ResendOtpClicked) }
                )
            }

            EmailButtonState.CREATE_PASSWORD -> {
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
                EmailButtonState.CONTINUE_WITH_EMAIL ->
                    onEvent(SignUpIntent.ContinueWithEmailClicked)

                EmailButtonState.CREATE_ACCOUNT ->
                    onEvent(SignUpIntent.CreateAccountClicked)

                EmailButtonState.VERIFY_EMAIL ->
                    onEvent(SignUpIntent.VerifyEmailClicked)

                EmailButtonState.CREATE_PASSWORD ->
                    onEvent(SignUpIntent.CreatePasswordClicked)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// =====================================================
// HEADER COMPONENTS
// =====================================================
@Composable
fun SignUpHeader(toolbarTitle: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.iv_back),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
        Text(
            text = toolbarTitle,
            fontSize = 20.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_condensed_regular,
                    FontWeight.SemiBold
                )
            ),
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        // Balance space for centered title
        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun WelcomeText() {
    Text(
        text = "Begin with creating new free account. This helps you keep your learning way easier.",
        fontSize = 16.sp,
        fontFamily = FontFamily(
            Font(R.font.roboto_condensed_regular, FontWeight.Normal)
        ),
        color = Color(0xFF64748B),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
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
// EMAIL VIEW
// =====================================================

@Composable
private fun ShowEmailView(email: String, emailError: String?, onEmailChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {

        InputLabel(text = "Email")

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("example@example.com", color = Color(0xFF9CA3AF)) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6366F1),
                unfocusedBorderColor = Color(0xFFD1D5DB),
                errorBorderColor = Color(0xFFDC2626),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            isError = emailError != null
        )
        if (emailError != null) {
            Text(
                text = emailError,
                color = Color(0xFFDC2626),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// =====================================================
// PASSWORD VIEW
// =====================================================

@Composable
private fun ShowPasswordView(
    password: String,
    passwordError: String?,
    onPasswordChange: (String) -> Unit,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Password validation
    val hasMinLength = password.length >= 8
    val hasNumber = password.any { it.isDigit() }
    val hasSymbol = password.any { !it.isLetterOrDigit() }
    val satisfiedRulesCount = listOf(hasMinLength, hasNumber, hasSymbol).count { it }

    // Progress animation
    val targetProgress = satisfiedRulesCount / 3f
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = 450,
            easing = FastOutSlowInEasing
        ),
        label = "PasswordStrengthProgress"
    )
    val progressColor = when (satisfiedRulesCount) {
        0, 1 -> Color(0xFFDC2626) // red
        2 -> Color(0xFFF59E0B)    // amber
        else -> Color(0xFF22C55E) // green
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        InputLabel(text = "Password")
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = { Text("********", color = Color.Gray) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6366F1),
                unfocusedBorderColor = Color(0xFFD1D5DB),
                errorBorderColor = Color(0xFFDC2626),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),

            visualTransformation = if (isPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible)
                            "Hide password"
                        else
                            "Show password",
                        tint = Color(0xFF6B7280)
                    )
                }
            },
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            isError = passwordError != null
        )

        if (passwordError != null) {
            Text(
                text = passwordError,
                color = Color(0xFFDC2626),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = {
                animatedProgress
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = progressColor,
            trackColor = Color(0xFFE5E7EB)
        )
        Spacer(modifier = Modifier.height(16.dp))

        PasswordRule(text = "8 characters minimum", isValid = hasMinLength)
        PasswordRule(text = "At least one number", isValid = hasNumber)
        PasswordRule(text = "At least one symbol", isValid = hasSymbol)
    }
}

@Composable
private fun PasswordRule(
    text: String,
    isValid: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = if (isValid)
                Icons.Default.CheckCircle
            else
                Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isValid) Color(0xFF22C55E) else Color(0xFF9CA3AF),
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isValid) Color(0xFF22C55E) else Color(0xFF6B7280)
        )
    }
}

// =====================================================
// OTP VIEW
// =====================================================

@Composable
private fun ShowEmailOtpView(
    email: String,
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
            text = "We just sent 5-digit code to $email, enter it below:",
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
        Log.d(TAG, "OTP changed from parent: '$otp'")
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
                        Log.e(TAG, "OtpInputField: $newOtp")
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
fun InputLabel(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontFamily = FontFamily(
            Font(
                R.font.roboto_condensed_regular,
                FontWeight.SemiBold
            )
        ),
        color = Color.Black,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(onNavigateToHome = {}, onNavigateBack = {})
}