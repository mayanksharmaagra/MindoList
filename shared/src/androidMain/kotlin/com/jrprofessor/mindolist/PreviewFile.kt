package com.jrprofessor.mindolist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordState
import com.jrprofessor.mindolist.screen.EditProfileContent
import com.jrprofessor.mindolist.screen.ForgotPasswordContent

//@Preview
//@Composable
//fun ForgotPasswordScreenPreview() {
//    ForgotPasswordContent(
//        state = ForgotPasswordState(),
//        onBackPressed = {},
//        onEmailChange = {},
//        onContinueClicked = {},
//        onNavigateBack = {}
//    )
//}

@Preview
@Composable
fun EditProfileScreenPreview() {
    EditProfileContent(
        state = com.jrprofessor.mindolist.presentation.editProfile.EditProfileState(),
        onAction = {},
        onBackClick = {}
    )
}
