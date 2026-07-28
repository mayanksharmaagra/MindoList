package com.jrprofessor.mindolist.utils

import androidx.compose.runtime.Composable

@Composable
expect fun rememberGoogleSignInLauncher(
    onSuccess: (accessToken: String) -> Unit,
    onError: (String) -> Unit
): () -> Unit
