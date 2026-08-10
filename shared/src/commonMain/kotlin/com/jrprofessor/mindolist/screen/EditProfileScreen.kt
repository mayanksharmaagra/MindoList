package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.jrprofessor.mindolist.icons.IcUser
import com.jrprofessor.mindolist.presentation.editProfile.EditProfileAction
import com.jrprofessor.mindolist.presentation.editProfile.EditProfileEvent
import com.jrprofessor.mindolist.presentation.editProfile.EditProfileState
import com.jrprofessor.mindolist.theme.MindoListAccentFixed
import com.jrprofessor.mindolist.theme.MindoListTheme
import com.jrprofessor.mindolist.utils.RequestCameraPermission
import com.jrprofessor.mindolist.utils.RequestStoragePermission
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.EditProfileViewModel
import com.preat.peekaboo.image.picker.FilterOptions
import com.preat.peekaboo.image.picker.ResizeOptions
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
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
                is EditProfileEvent.ShowToast -> showToast(event.message)
                EditProfileEvent.NavigateBack -> onBackClick()
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
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var showCamera by remember { mutableStateOf(false) }
    var showImageSourceOption by remember { mutableStateOf(false) }

    var requestCameraPermission by remember { mutableStateOf(false) }
    var requestStoragePermission by remember { mutableStateOf(false) }

    val launcher = rememberImagePickerLauncher(
        resizeOptions = ResizeOptions(width = 512, height = 512),
        scope = scope,
        filterOptions = FilterOptions.Default,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let { onAction(EditProfileAction.OnAvatarChange(it)) }
        }
    )

    if (requestCameraPermission) {
        RequestCameraPermission(
            onPermissionGranted = {
                requestCameraPermission = false
                showCamera = true
            },
            onPermissionDenied = {
                requestCameraPermission = false
                showToast("Camera permission is required to take photos")
            }
        )
    }

    if (requestStoragePermission) {
        RequestStoragePermission(
            onPermissionGranted = {
                requestStoragePermission = false
                launcher.launch()
            },
            onPermissionDenied = {
                requestStoragePermission = false
                showToast("Storage permission is required to pick photos")
            }
        )
    }

    if (showImageSourceOption) {
        ImageSourceOptionDialog(
            onDismissRequest = { showImageSourceOption = false },
            onGalleryClick = {
                showImageSourceOption = false
                requestStoragePermission = true
            },
            onCameraClick = {
                showImageSourceOption = false
                requestCameraPermission = true
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MindoListTheme.colors.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                EditProfileHeader(
                    onBackClick = onBackClick,
                )
                Spacer(modifier = Modifier.height(32.dp))

                ProfilePhotoSection(
                    state = state,
                    onChangePhotoClick = { showImageSourceOption = true },
                    onRemovePhotoClick = { onAction(EditProfileAction.OnRemovePhoto) }
                )

                Spacer(modifier = Modifier.height(40.dp))

                BasicInfoSection(state, onAction)

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = { 
                        keyboardController?.hide()
                        onAction(EditProfileAction.OnSaveClick) 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MindoListAccentFixed)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Black
                        )
                    } else {
                        Text(
                            "Save changes",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }

            if (showCamera) {
                val cameraState = rememberPeekabooCameraState(onCapture = { byteArrays ->
                    byteArrays?.let { onAction(EditProfileAction.OnAvatarChange(it)) }
                    showCamera = false
                })
                Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                    PeekabooCamera(
                        state = cameraState,
                        modifier = Modifier.fillMaxSize(),
                        permissionDeniedContent = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Camera permission denied", color = Color.White)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(onClick = { showCamera = false }) {
                                        Text("Go Back")
                                    }
                                }
                            }
                        }
                    )

                    IconButton(
                        onClick = { showCamera = false },
                        modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Camera",
                            tint = Color.White
                        )
                    }

                    // Capture Button
                    Surface(
                        onClick = { cameraState.capture() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 40.dp)
                            .size(70.dp),
                        shape = RoundedCornerShape(35.dp),
                        color = Color.White,
                        border = BorderStroke(4.dp, Color.Gray)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Surface(
                                modifier = Modifier.size(54.dp),
                                shape = RoundedCornerShape(27.dp),
                                color = Color.White,
                                border = BorderStroke(2.dp, Color.Black)
                            ) {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageSourceOptionDialog(
    onDismissRequest: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Choose Profile Photo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onGalleryClick() }.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        tint = MindoListTheme.colors.textPrimary
                    )
                    Text("Gallery", color = MindoListTheme.colors.textPrimary)
                }
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onCameraClick() }.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = MindoListTheme.colors.textPrimary
                    )
                    Text("Camera", color = MindoListTheme.colors.textPrimary)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        containerColor = MindoListTheme.colors.cardBg,
        titleContentColor = MindoListTheme.colors.textPrimary,
        textContentColor = MindoListTheme.colors.textPrimary
    )
}

@Composable
fun EditProfileHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            onClick = onBackClick,
            modifier = Modifier.size(44.dp),
            shape = RoundedCornerShape(12.dp),
            color = MindoListTheme.colors.cardChildBg,
            border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
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
            text = "Edit Profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MindoListTheme.colors.textPrimary,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ProfilePhotoSection(
    state: EditProfileState,
    onChangePhotoClick: () -> Unit,
    onRemovePhotoClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.clickable { onChangePhotoClick() }
        ) {
            Surface(
                modifier = Modifier.size(140.dp),
                shape = RoundedCornerShape(40.dp),
                color = MindoListAccentFixed
            ) {
                Box(contentAlignment = Alignment.Center) {
                    val imageSource: Any? = state.avatarBytes ?: state.avatarUrl
                    if ((imageSource!=null || imageSource.toString()!="null") &&  imageSource.toString()!="") {
                        AsyncImage(
                            model = imageSource,
                            contentDescription = "Profile",
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(40.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = state.fullName.firstOrNull()?.toString()?.uppercase() ?: "A",
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier.size(40.dp).offset(x = 4.dp, y = 4.dp),
                shape = RoundedCornerShape(12.dp),
                color = MindoListTheme.colors.cardChildBg,
                border = BorderStroke(2.dp, MindoListTheme.colors.background)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change Photo",
                        tint = MindoListTheme.colors.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Change photo",
            color = MindoListTheme.colors.accent,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onChangePhotoClick() }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Remove current photo",
            color = MindoListTheme.colors.textSecondary,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onRemovePhotoClick() }
        )
    }
}

@Composable
fun BasicInfoSection(state: EditProfileState, onAction: (EditProfileAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(
            text = "BASIC INFO",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MindoListTheme.colors.textSecondary,
            letterSpacing = 1.sp
        )

        EditField(
            label = "FULL NAME",
            value = state.fullName,
            onValueChange = { onAction(EditProfileAction.OnFullNameChange(it)) },
            icon = IcUser,
            maxLength = 50
        )

        EditField(
            label = "EMAIL ADDRESS",
            value = state.email,
            onValueChange = { onAction(EditProfileAction.OnEmailChange(it)) },
            icon = Icons.Outlined.Email,
            maxLength = 50,
            trailingContent = {
                if (state.isVerified) {
                    Surface(
                        color = Color(0xFF064E3B),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Check, null, tint = MindoListTheme.colors.success, modifier = Modifier.size(14.dp))
                            Text("Verified", color = MindoListTheme.colors.success, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        )

        EditField(
            label = "PHONE NUMBER (optional)",
            value = state.phoneNumber,
            onValueChange = { onAction(EditProfileAction.OnPhoneNumberChange(it)) },
            icon = Icons.Outlined.Phone,
            placeholder = "+91 00000 00000",
            maxLength = 15
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "ABOUT (optional)",
                color = MindoListTheme.colors.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.about,
                    onValueChange = { 
                        if (it.length <= 200) {
                            onAction(EditProfileAction.OnAboutChange(it)) 
                        }
                    },
                    placeholder = { Text("A short line about yourself", color = MindoListTheme.colors.textSecondary) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                        unfocusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                        focusedContainerColor = MindoListTheme.colors.inputBg,
                        unfocusedContainerColor = MindoListTheme.colors.inputBg,
                        focusedTextColor = MindoListTheme.colors.textPrimary,
                        unfocusedTextColor = MindoListTheme.colors.textPrimary
                    )
                )
                Text(
                    text = "${state.about.length}/200",
                    color = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(end = 12.dp, bottom = 12.dp)
                )
            }
            Text(
                "Shown only to you — not shared anywhere.",
                color = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    placeholder: String = "",
    maxLength: Int = Int.MAX_VALUE,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label,
            color = MindoListTheme.colors.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.length <= maxLength) {
                    onValueChange(it)
                }
            },
            placeholder = { Text(placeholder, color = MindoListTheme.colors.textSecondary) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = {
                Icon(icon, null, tint = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
            },
            trailingIcon = trailingContent,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                unfocusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                focusedContainerColor = MindoListTheme.colors.inputBg,
                unfocusedContainerColor = MindoListTheme.colors.inputBg,
                focusedTextColor = MindoListTheme.colors.textPrimary,
                unfocusedTextColor = MindoListTheme.colors.textPrimary
            )
        )
    }
}
