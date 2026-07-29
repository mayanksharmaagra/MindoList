package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.customView.MindoLogo
import com.jrprofessor.mindolist.customView.ShowEmailView
import com.jrprofessor.mindolist.customView.ShowNameView
import com.jrprofessor.mindolist.customView.ShowPasswordView
import com.jrprofessor.mindolist.presentation.login.LoginEvent
import com.jrprofessor.mindolist.presentation.login.LoginIntent
import com.jrprofessor.mindolist.presentation.signup.SignUpButtonState
import com.jrprofessor.mindolist.presentation.signup.SignUpEvent
import com.jrprofessor.mindolist.presentation.signup.SignUpIntent
import com.jrprofessor.mindolist.theme.*
import com.jrprofessor.mindolist.utils.StatusBarDarkMode
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.LoginViewModel
import com.jrprofessor.mindolist.viewmodels.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest
import mindolist.shared.generated.resources.Res
import mindolist.shared.generated.resources.app_logo
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WelcomeScreen(
    loginViewModel: LoginViewModel = koinViewModel(),
    signUpViewModel: SignUpViewModel = koinViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToSignUpStep: () -> Unit,
    onNavigateToForgot: () -> Unit,
) {
    StatusBarDarkMode()

    var isLoginMode by remember { mutableStateOf(value = true) }
    var confirmPassword by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }

    val loginState by loginViewModel.loginState.collectAsState()
    val signUpState by signUpViewModel.signUpState.collectAsState()

    val ledgerGradient = Brush.linearGradient(
        colors = listOf(MindoListAccentFixed, MindoListSecond)
    )

    // Collect Login Effects
    LaunchedEffect(Unit) {
        loginViewModel.loginEffect.collectLatest { effect ->
            when (effect) {
                LoginEvent.NavigateToHome -> onNavigateToHome()
                LoginEvent.NavigateToForgotPassword -> onNavigateToForgot()
                is LoginEvent.ShowError -> showToast(effect.error)
                is LoginEvent.ShowToast -> showToast(effect.message)
                else -> Unit
            }
        }
    }

    // Collect SignUp Effects
    LaunchedEffect(Unit) {
        signUpViewModel.signUpEffect.collectLatest { effect ->
            when (effect) {
                SignUpEvent.NavigateToHome -> onNavigateToHome()
                SignUpEvent.NavigateToVerifyEmail -> onNavigateToSignUpStep()
                is SignUpEvent.ShowError -> showToast(effect.error)
                is SignUpEvent.ShowToast -> showToast(effect.message)
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MindoListTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Branding Header
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ledgerGradient),
                contentAlignment = Alignment.Center
            ) {
//                Icon(
//                    imageVector = Icons.Default.Check,
//                    contentDescription = null,
//                    tint = Color.Black,
//                    modifier = Modifier.size(24.dp)
//                )
                Image(
                    painter = painterResource(Res.drawable.app_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(36.dp)
                )
//                MindoLogo(modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "MindoList",
                color = MindoListTheme.colors.textSecondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Dynamic Title
        Text(
            text = if (isLoginMode) "Welcome back.\nLet's clear today's list." else "Create your account",
            color = MindoListTheme.colors.textPrimary,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 40.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Dynamic Subtitle
        Text(
            text = if (isLoginMode) "Sign in to sync your tasks across devices." else "Start your ledger — it takes less than a minute.",
            color = MindoListTheme.colors.textSecondary,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Login/Signup Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MindoListTheme.colors.inputBg)
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .then(
                        if (isLoginMode) Modifier.background(ledgerGradient)
                        else Modifier.background(Color.Transparent)
                    )
                    .clickable {
                        isLoginMode = true
                        confirmPassword = ""
                        isAgreed = false
                        loginViewModel.handleLoginEvent(LoginIntent.ClearState)
                        signUpViewModel.handleEvent(SignUpIntent.ClearState)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Log in",
                    color = if (isLoginMode) Color.Black else MindoListTheme.colors.textSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .then(
                        if (!isLoginMode) Modifier.background(ledgerGradient)
                        else Modifier.background(Color.Transparent)
                    )
                    .clickable {
                        isLoginMode = false
                        confirmPassword = ""
                        isAgreed = false
                        loginViewModel.handleLoginEvent(LoginIntent.ClearState)
                        signUpViewModel.handleEvent(SignUpIntent.ClearState)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign up",
                    color = if (!isLoginMode) Color.Black else MindoListTheme.colors.textSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Form Fields
        if (!isLoginMode) {
            ShowNameView(
                name = signUpState.name,
                nameError = signUpState.nameError,
                labelColor = MindoListTheme.colors.textSecondary,
                textColor = MindoListTheme.colors.textPrimary,
                containerColor = MindoListTheme.colors.inputBg,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MindoListTheme.colors.accent,
                onNameChange = { signUpViewModel.handleEvent(SignUpIntent.NameChanged(it)) }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        ShowEmailView(
            email = if (isLoginMode) loginState.email else signUpState.email,
            emailError = if (isLoginMode) loginState.emailError else signUpState.emailError,
            labelColor = MindoListTheme.colors.textSecondary,
            textColor = MindoListTheme.colors.textPrimary,
            containerColor = MindoListTheme.colors.inputBg,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = MindoListTheme.colors.accent,
            onEmailChange = {
                if (isLoginMode) {
                    loginViewModel.handleLoginEvent(LoginIntent.EmailChanged(it))
                } else {
                    signUpViewModel.handleEvent(SignUpIntent.EmailChanged(it))
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        ShowPasswordView(
            password = if (isLoginMode) loginState.password else signUpState.password,
            passwordError = if (isLoginMode) loginState.passwordError else signUpState.passwordError,
            label = "Password",
            labelColor = MindoListTheme.colors.textSecondary,
            textColor = MindoListTheme.colors.textPrimary,
            containerColor = MindoListTheme.colors.inputBg,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = MindoListTheme.colors.accent,
            placeholderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f),
            showPasswordRule = !isLoginMode,
            onPasswordChange = {
                if (isLoginMode) {
                    loginViewModel.handleLoginEvent(LoginIntent.PasswordChanged(it))
                } else {
                    signUpViewModel.handleEvent(SignUpIntent.PasswordChanged(it))
                }
            }
        )

        if (!isLoginMode) {
            Spacer(modifier = Modifier.height(24.dp))
            ShowPasswordView(
                password = confirmPassword,
                passwordError = null,
                label = "Confirm Password",
                labelColor = MindoListTheme.colors.textSecondary,
                textColor = MindoListTheme.colors.textPrimary,
                containerColor = MindoListTheme.colors.inputBg,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MindoListTheme.colors.accent,
                placeholderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f),
                showPasswordRule = false,
                onPasswordChange = { confirmPassword = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoginMode) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Remember me",
                    color = MindoListTheme.colors.textSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = "Forgot password?",
                    color = MindoListTheme.colors.accent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        loginViewModel.handleLoginEvent(LoginIntent.ForgotPasswordClicked)
                    }
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isAgreed,
                    onCheckedChange = { isAgreed = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MindoListTheme.colors.accent,
                        uncheckedColor = MindoListTheme.colors.textSecondary,
                        checkmarkColor = Color.Black
                    )
                )
                Text(
                    text = buildAnnotatedString {
                        append("I agree to the ")
                        withStyle(
                            style = SpanStyle(
                                color = MindoListTheme.colors.accent,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Terms of Service")
                        }
                        append(" and ")
                        withStyle(
                            style = SpanStyle(
                                color = MindoListTheme.colors.accent,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Privacy Policy")
                        }
                    },
                    color = MindoListTheme.colors.textPrimary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        val isLoading = if (isLoginMode) loginState.isLoading else signUpState.isLoading
        val isEnabled = if (isLoginMode) {
            loginState.isEmailValid && loginState.isPasswordValid && !isLoading
        } else {
            (signUpState.isNameValid && signUpState.isEmailValid && signUpState.isPasswordValid && isAgreed && confirmPassword == signUpState.password && !isLoading)
        }

        ActionButton(
            text = if (isLoginMode) "Log in" else "Create account",
            isLoading = isLoading,
            isEnabled = isEnabled,
            containerColor = MindoListAccentFixed,
            textColor = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                if (isLoginMode) {
                    loginViewModel.handleLoginEvent(LoginIntent.LoginClicked)
                } else {
                    signUpViewModel.handleEvent(SignUpIntent.SendVerificationCode)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isLoginMode) "Don't have an account? " else "Already have an account? ",
                color = MindoListTheme.colors.textSecondary,
                fontSize = 14.sp
            )
            Text(
                text = if (isLoginMode) "Sign up free" else "Log in",
                color = MindoListTheme.colors.accent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    isLoginMode = !isLoginMode
                    confirmPassword = ""
                    isAgreed = false
                    loginViewModel.handleLoginEvent(LoginIntent.ClearState)
                    signUpViewModel.handleEvent(SignUpIntent.ClearState)
                }
            )
        }
    }
}
