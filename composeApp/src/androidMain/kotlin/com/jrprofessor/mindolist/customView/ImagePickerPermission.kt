package com.jrprofessor.mindolist.customView

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


@Composable
fun ImagePickerWithPermission(
    selectedImageUri: Uri? = null,
    onImageSelected: (Uri) -> Unit,
    onRemoveImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

//    ProfilePictureWithEdit(
//        imageUrl = selectedImageUri.toString(),
//        onEditClick = {
//            when {
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
//                    // Android 13+ - Request READ_MEDIA_IMAGES
//                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
//                }
//
//                else -> {
//                    // Android 12 and below - Request READ_EXTERNAL_STORAGE
//                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//                }
//            }
//        },
//        onRemoveClick = onRemoveImage,
//        modifier = modifier
//    )
}