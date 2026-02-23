package com.jrprofessor.mindolist.welcomeScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.customView.ShowEmailView
import com.jrprofessor.mindolist.customView.ShowPasswordView
import com.jrprofessor.mindolist.customView.SignUpHeader
import com.jrprofessor.mindolist.customView.WelcomeText
import com.jrprofessor.mindolist.presentation.LoginEvent
import com.jrprofessor.mindolist.presentation.LoginIntent
import com.jrprofessor.mindolist.presentation.LoginMode
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.theme.btnColor
import com.jrprofessor.mindolist.utils.StatusBarInDarkMode
import kotlinx.coroutines.flow.collectLatest

const val SIGN_IN_TAG = "LoginScreen"

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit = {},
    onNavigateToForgot: () -> Unit
) {
    val state by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    val btnTitle = when (state.currentMode) {
        LoginMode.INITIAL -> "Continue with email"
        LoginMode.EMAIL_PASSWORD -> "Log In"
    }
    // Button enable state based on current screen
    val isEnabled = when (state.currentMode) {
        LoginMode.INITIAL -> true
        LoginMode.EMAIL_PASSWORD -> state.isEmailValid && state.isPasswordValid
    }

    StatusBarInDarkMode()

    LaunchedEffect(Unit) {
        viewModel.loginEffect.collectLatest { effect ->
            when (effect) {
                LoginEvent.NavigateBack -> onNavigateBack()
                LoginEvent.NavigateToHome -> onNavigateToHome()
                LoginEvent.NavigateToForgotPassword -> onNavigateToForgot()
                is LoginEvent.ShowError -> Toast.makeText(
                    context,
                    effect.error,
                    Toast.LENGTH_SHORT
                ).show()

                is LoginEvent.ShowToast -> Toast.makeText(
                    context,
                    effect.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        SignUpHeader(
            toolbarTitle = "Log into account",
            onBackClick = {
                viewModel.handleLoginEvent(LoginIntent.BackPressed)
            }
        )
        Spacer(modifier = Modifier.height(30.dp))

        // Subtitle or Step Indicator
        if (state.currentMode == LoginMode.INITIAL) {
            WelcomeText("Welcome back!\nLet’s continue learning")
        }
        Spacer(modifier = Modifier.height(30.dp))
        // Content based on state
        when (state.currentMode) {
            LoginMode.EMAIL_PASSWORD -> {
                ShowEmailView(
                    email = state.email,
                    emailError = state.emailError,
                    onEmailChange = {
                        viewModel.handleLoginEvent(LoginIntent.EmailChanged(it))
                    }
                )
                Spacer(modifier = Modifier.height(15.dp))
                ShowPasswordView(
                    password = state.password,
                    passwordError = state.passwordError,
                    showPasswordRule = false,
                    onPasswordChange = {
                        viewModel.handleLoginEvent(LoginIntent.PasswordChanged(it))
                    }
                )
            }

            else -> Unit
        }

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
            when (state.currentMode) {
                LoginMode.INITIAL -> {
                    viewModel.handleLoginEvent(LoginIntent.ContinueWithEmailClicked)
                }

                LoginMode.EMAIL_PASSWORD -> {
                    viewModel.handleLoginEvent(LoginIntent.LoginClicked)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        if(state.currentMode== LoginMode.EMAIL_PASSWORD){
            // Resend OTP button
            TextButton(
                onClick = {
                    viewModel.handleLoginEvent(LoginIntent.ForgotPasswordClicked)
                },
            ) {
                Text(
                    text = "Forgot password?",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
            // Balance space for centered title
            Spacer(modifier = Modifier.size(30.dp))
        }
    }
}

@Preview
@Composable
fun SignInScreenPreview() {

}