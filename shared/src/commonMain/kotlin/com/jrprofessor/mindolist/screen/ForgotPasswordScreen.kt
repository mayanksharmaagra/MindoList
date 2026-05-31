package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.customView.ShowEmailView
import com.jrprofessor.mindolist.customView.SignUpHeader
import com.jrprofessor.mindolist.customView.WelcomeText
import com.jrprofessor.mindolist.icons.IcLockEmail
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordEvent
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordIntent
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordState
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordStep
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.theme.btnColor
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
        onBackPressed = {
            viewModel.forgotPasswordEventHandle(ForgotPasswordIntent.BackPressed)
        },
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
    onBackPressed: () -> Unit,
    onEmailChange: (String) -> Unit,
    onContinueClicked: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val btnTitle = when (state.currentStep) {
        ForgotPasswordStep.ENTER_EMAIL -> "Send"
        else -> "Back to Login"
    }
    val isEnabled = when (state.currentStep) {
        ForgotPasswordStep.ENTER_EMAIL -> state.isEmailValid
        else -> true
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
            toolbarTitle = "Reset Password",
            onBackClick = onBackPressed
        )
        Spacer(modifier = Modifier.height(30.dp))
        if (state.currentStep == ForgotPasswordStep.ENTER_EMAIL) {
            WelcomeText("We will email you a link to reset your password..")
            Spacer(modifier = Modifier.height(20.dp))
            ShowEmailView(
                email = state.email,
                emailError = state.emailError,
                onEmailChange = onEmailChange
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        if (state.currentStep == ForgotPasswordStep.EMAIL_SENT) {
            Image(
                imageVector = Icons.IcLockEmail,
                contentDescription = "Icon",
                modifier = Modifier.size(150.dp),
            )
            WelcomeText("We have sent an email to ${state.email} with instructions to reset your password.")
        }
        Spacer(modifier = Modifier.height(20.dp))

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
                ForgotPasswordStep.ENTER_EMAIL -> onContinueClicked()

                else -> {
                    onNavigateBack()
                }
            }
        }
    }
}
