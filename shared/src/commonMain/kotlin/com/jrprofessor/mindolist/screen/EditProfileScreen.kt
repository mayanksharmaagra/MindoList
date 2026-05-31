package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.customView.ProfileImageSection
import com.jrprofessor.mindolist.customView.WelcomeText
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.icons.SecurityIcon
import com.jrprofessor.mindolist.presentation.editProfile.EditProfileAction
import com.jrprofessor.mindolist.presentation.editProfile.EditProfileEvent
import com.jrprofessor.mindolist.presentation.editProfile.EditProfileState
import com.jrprofessor.mindolist.theme.PrimaryBlue
import com.jrprofessor.mindolist.theme.TextHeadingColor
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.theme.btnColor
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.EditProfileViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is EditProfileEvent.ShowToast -> {
                    showToast(event.message)
                }
                EditProfileEvent.NavigateBack -> {
                    onBackClick()
                }
            }
        }
    }

    EditProfileContent(
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick
    )
}

@Composable
fun EditProfileContent(
    state: EditProfileState,
    onAction: (EditProfileAction) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        ToolBarWithBackButton(onBackClick)
        Spacer(modifier = Modifier.height(20.dp))
        ProfileImage(
            userName = state.fullName,
            avatarUrl = state.avatarUrl,
            onAvatarChange = { onAction(EditProfileAction.OnAvatarChange(it)) }
        )
        Spacer(modifier = Modifier.height(20.dp))
        UserIdentity(
            fullName = state.fullName,
            email = state.email,
            onNameChange = { onAction(EditProfileAction.OnFullNameChange(it)) },
            onEmailChange = { onAction(EditProfileAction.OnEmailChange(it)) }
        )
        Spacer(modifier = Modifier.height(20.dp))
        UserPassword(
            currentPassword = state.currentPassword,
            newPassword = state.newPassword,
            confirmPassword = state.confirmPassword,
            onCurrentPasswordChange = { onAction(EditProfileAction.OnCurrentPasswordChange(it)) },
            onNewPasswordChange = { onAction(EditProfileAction.OnNewPasswordChange(it)) },
            onConfirmPasswordChange = { onAction(EditProfileAction.OnConfirmPasswordChange(it)) }
        )
        Spacer(modifier = Modifier.height(40.dp))
        ActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(24.dp),
            text = "Save Changes",
            fontSize = 18.sp,
            textColor = Color.White,
            containerColor = btnColor,
            isIconVisible = false,
            isLoading = state.isLoading,
            isEnabled = state.fullName.isNotBlank() && state.email.isNotBlank() && !state.isLoading
        ) {
            onAction(EditProfileAction.OnSaveClick)
        }
        Spacer(modifier = Modifier.height(100.dp))

    }
}

@Composable
fun UserPassword(
    currentPassword: String,
    newPassword: String,
    confirmPassword: String,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit
) {
    var isCurrentPasswordVisible by remember { mutableStateOf(false) }
    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row {
                Icon(
                    imageVector = Icons.SecurityIcon,
                    contentDescription = "Security",
                    tint = btnColor,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SECURITY",
                    fontSize = 16.sp,
                    color = TextHeadingColor,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            InputLabel(
                text = "Current Password",
                fontWeight = FontWeight.SemiBold,
                textColor = Color(0xFF1C1C1C)
            )
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = currentPassword,
                onValueChange = onCurrentPasswordChange,
                placeholder = { Text("Enter Password", color = Color(0xFF9CA3AF)) },
                singleLine = true,
                visualTransformation = if (isCurrentPasswordVisible) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF9FAFB),
                    unfocusedContainerColor = Color(0xFFF9FAFB),
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color.Black
                ),
                textStyle = LocalTextStyle.current.copy(
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    IconButton(onClick = { isCurrentPasswordVisible = !isCurrentPasswordVisible }) {
                        Icon(
                            imageVector = if (isCurrentPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (isCurrentPasswordVisible)
                                "Hide password"
                            else
                                "Show password",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputLabel(
                text = "New Password",
                fontWeight = FontWeight.SemiBold,
                textColor = Color(0xFF1C1C1C)
            )
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                placeholder = { Text("Min. 8 characters", color = Color(0xFF9CA3AF)) },
                singleLine = true,
                visualTransformation = if (isNewPasswordVisible) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF9FAFB),
                    unfocusedContainerColor = Color(0xFFF9FAFB),
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color.Black
                ),
                textStyle = LocalTextStyle.current.copy(
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                        Icon(
                            imageVector = if (isNewPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (isNewPasswordVisible)
                                "Hide password"
                            else
                                "Show password",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputLabel(
                text = "Confirm Password",
                fontWeight = FontWeight.SemiBold,
                textColor = Color(0xFF1C1C1C)
            )
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                placeholder = { Text("Repeat new password", color = Color(0xFF9CA3AF)) },
                singleLine = true,
                visualTransformation = if (isConfirmPasswordVisible) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF9FAFB),
                    unfocusedContainerColor = Color(0xFFF9FAFB),
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color.Black
                ),
                textStyle = LocalTextStyle.current.copy(
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (isConfirmPasswordVisible)
                                "Hide password"
                            else
                                "Show password",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun UserIdentity(
    fullName: String,
    email: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp)) {
            Text(
                text = "IDENTITY",
                fontSize = 16.sp,
                color = TextHeadingColor,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputLabel(text = "Full Name", fontWeight = FontWeight.SemiBold, textColor = Color(0xFF1C1C1C))
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = fullName,
                onValueChange = onNameChange,
                placeholder = { Text("Full Name", color = Color(0xFF9CA3AF)) },
                singleLine = true,
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF9FAFB),
                    unfocusedContainerColor = Color(0xFFF9FAFB),
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color.Black
                ),
                textStyle = LocalTextStyle.current.copy(
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputLabel(text = "Email", fontWeight = FontWeight.SemiBold, textColor = Color(0xFF1C1C1C))
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                placeholder = { Text("example@example.com", color = Color(0xFF9CA3AF)) },
                singleLine = true,
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF9FAFB),
                    unfocusedContainerColor = Color(0xFFF9FAFB),
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color.Black
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
            )
        }
    }
}

@Composable
fun ProfileImage(
    userName: String?,
    avatarUrl: String?,
    onAvatarChange: (ByteArray) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        ProfileImageSection(
            userName = userName,
            avatarUrl = avatarUrl,
            onAvatarEditClick = onAvatarChange,
            isEdit = true
        )
        WelcomeText("Change Profile Picture", PrimaryBlue)
        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
fun ToolBarWithBackButton(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            androidx.compose.material3.Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
            )
        }
        Text(
            text = "Edit Profile",
            fontSize = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.SemiBold
        )
        // Right - Spacer (same size as IconButton to balance)
        Spacer(modifier = Modifier.size(48.dp))

    }
}