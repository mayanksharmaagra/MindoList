package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.CircularProgressIndicator
import com.jrprofessor.mindolist.extension.toTimeAgo
import com.jrprofessor.mindolist.icons.GoogleLogo
import com.jrprofessor.mindolist.presentation.dashboard.DashboardAction
import com.jrprofessor.mindolist.theme.MindoListAccentFixed
import com.jrprofessor.mindolist.theme.MindoListMintFixed
import com.jrprofessor.mindolist.theme.MindoListTheme
import com.jrprofessor.mindolist.utils.GoogleAuthManager
import com.jrprofessor.mindolist.utils.rememberGoogleSignInLauncher
import com.jrprofessor.mindolist.viewmodels.DashboardViewModel
import com.jrprofessor.mindolist.viewmodels.GoogleCalendarViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun IntegrationsScreen(
    onBackClick: () -> Unit,
    viewModel: GoogleCalendarViewModel = koinViewModel(),
    dashboardViewModel: DashboardViewModel = koinViewModel(),
    authManager: GoogleAuthManager = koinInject()
) {
    var autoSync by remember { mutableStateOf(true) }
    val dashboardState by dashboardViewModel.state.collectAsState()
    val mainUser = dashboardState.user

    val isGoogleConnected = mainUser?.isGoogleConnected == true
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    val googleSignInLauncher = rememberGoogleSignInLauncher(
        onSuccess = { accessToken ->
            scope.launch {
                val googleUser = authManager.userData.value
                if (googleUser != null && mainUser != null) {
                    val googleEmail = googleUser.googleEmail
                    if (googleEmail != null && googleEmail != mainUser.email) {
                        snackbarHostState.showSnackbar("Email mismatch: Please use ${mainUser.email}")
                        authManager.signOut()
                    } else if (googleEmail != null) {
                        viewModel.updateGoogleIntegration(
                            googleEmail = googleEmail,
                            accessToken = accessToken
                        )
                    }
                }
            }
        },
        onError = { errorMessage ->
            Napier.e("Google Sign-In Error: $errorMessage", tag = "IntegrationsScreen")
            scope.launch {
                snackbarHostState.showSnackbar(errorMessage)
            }
        }
    )

    LaunchedEffect(error) {
        error?.let {
            Napier.e("Integrations Error: $it", tag = "IntegrationsScreen")
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MindoListTheme.colors.background
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            color = MindoListTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                IntegrationsHeader(onBackClick = onBackClick)

                if (!isGoogleConnected) {
                    Spacer(modifier = Modifier.height(30.dp))

                    LinkAccountsIllustration()

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "GET STARTED",
                        color = MindoListTheme.colors.mint,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Connect your first\nintegration",
                        color = MindoListTheme.colors.textPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Link your Google account to bring Calendar meetings and Google Tasks into TaskLedger — everything shows up alongside what you add yourself.",
                        color = MindoListTheme.colors.textSecondary,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    IntegrationFeatures()

                    Spacer(modifier = Modifier.height(48.dp))

                    Button(
                        onClick = { googleSignInLauncher() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = GoogleLogo,
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Connect Google account",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    val skipText = buildAnnotatedString {
                        append("Not ready? ")
                        withStyle(
                            SpanStyle(
                                color = MindoListTheme.colors.accent,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Skip for now")
                        }
                    }

                    Text(
                        text = skipText,
                        fontSize = 15.sp,
                        color = MindoListTheme.colors.textSecondary,
                        modifier = Modifier.clickable { onBackClick() }
                    )
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                    IntegrationsConnectedScreen(
                        connectedEmail = mainUser.googleEmail ?: "",
                        lastSyncedLabel = mainUser.googleLinkedAt.toTimeAgo(),
                        autoSyncEnabled = autoSync,
                        onAutoSyncToggle = { autoSync = it },
                        isSyncing = dashboardState.isGoogleSyncing,
                        onSyncNowClick = {
                            dashboardViewModel.dispatch(DashboardAction.RefreshGoogleTasks)
                        },
                        onDisconnectClick = {
                            viewModel.disconnectGoogleIntegration()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable(enabled = false) {}, // Prevent clicks through overlay
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MindoListTheme.colors.accent)
                }
            }
        }
    }
}

@Composable
fun IntegrationsConnectedScreen(
    connectedEmail: String,
    lastSyncedLabel: String,
    autoSyncEnabled: Boolean,
    isSyncing: Boolean,
    onAutoSyncToggle: (Boolean) -> Unit,
    onSyncNowClick: () -> Unit,
    onDisconnectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MindoListTheme.colors.background)
    ) {

        SectionEyebrow("CONNECTED")
        Spacer(Modifier.height(10.dp))

        ConnectedGoogleCard(
            connectedEmail = connectedEmail,
            lastSyncedLabel = lastSyncedLabel,
            autoSyncEnabled = autoSyncEnabled,
            isSyncing = isSyncing,
            onAutoSyncToggle = onAutoSyncToggle,
            onSyncNowClick = onSyncNowClick,
            onDisconnectClick = onDisconnectClick
        )

        Spacer(Modifier.height(28.dp))
        FooterNote()
    }
}

@Composable
private fun SectionEyebrow(text: String) {
    Text(
        text = text,
        color = MindoListTheme.colors.textSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.5.sp
    )
}

@Composable
private fun ConnectedGoogleCard(
    connectedEmail: String,
    lastSyncedLabel: String,
    autoSyncEnabled: Boolean,
    isSyncing: Boolean,
    onAutoSyncToggle: (Boolean) -> Unit,
    onSyncNowClick: () -> Unit,
    onDisconnectClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MindoListTheme.colors.cardBg,
        border = BorderStroke(1.dp, MindoListTheme.colors.mint.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            // ---- head: badge, title, subtitle, status pill ----
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MindoListTheme.colors.cardChildBg,
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = GoogleLogo,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Google Calendar & Tasks",
                        color = MindoListTheme.colors.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = "Importing meetings, tasks, and reminders.",
                        color = MindoListTheme.colors.textSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(Modifier.height(10.dp))
                    ConnectedStatusPill()
                }
            }

            Spacer(Modifier.height(14.dp))

            // ---- linked account row ----
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MindoListTheme.colors.cardChildBg
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(9.dp),
                        color = MindoListTheme.colors.accent,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = connectedEmail.first().uppercaseChar().toString(),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = connectedEmail,
                        color = MindoListTheme.colors.textPrimary,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (isSyncing) "Syncing..." else "Synced $lastSyncedLabel",
                        color = MindoListTheme.colors.textSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            HorizontalDivider()
            Spacer(Modifier.height(14.dp))

            // ---- auto-sync toggle row ----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Auto-sync",
                        color = MindoListTheme.colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Check for new items every hour",
                        color = MindoListTheme.colors.textSecondary,
                        fontSize = 10.5.sp
                    )
                }
                Switch(
                    checked = autoSyncEnabled,
                    onCheckedChange = onAutoSyncToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MindoListTheme.colors.background,
                        checkedTrackColor = MindoListTheme.colors.mint,
                        checkedBorderColor = Color.Transparent,
                        uncheckedThumbColor = MindoListTheme.colors.textSecondary,
                        uncheckedTrackColor = MindoListTheme.colors.cardChildBg,
                        uncheckedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f)
                    )
                )
            }

            Spacer(Modifier.height(16.dp))

            // ---- action buttons ----
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SyncNowButton(onClick = onSyncNowClick, isSyncing = isSyncing, modifier = Modifier.weight(1f))
                DisconnectButton(onClick = onDisconnectClick, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ConnectedStatusPill() {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MindoListTheme.colors.mint.copy(alpha = 0.15f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(MindoListTheme.colors.mint, CircleShape)
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = "Connected",
                color = MindoListTheme.colors.mint,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SyncNowButton(onClick: () -> Unit, isSyncing: Boolean, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(11.dp),
        color = MindoListTheme.colors.accent.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, MindoListTheme.colors.accent.copy(alpha = 0.4f)),
        onClick = { if (!isSyncing) onClick() },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 11.dp)
        ) {
            if (isSyncing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = MindoListTheme.colors.accent
                )
            } else {
                SyncGlyph(color = MindoListTheme.colors.accent)
            }
            Spacer(Modifier.width(6.dp))
            Text(
                text = if (isSyncing) "Syncing..." else "Sync now", 
                color = MindoListTheme.colors.accent, 
                fontSize = 12.5.sp, 
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DisconnectButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(11.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, MindoListTheme.colors.error.copy(alpha = 0.35f)),
        onClick = onClick,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = 11.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Disconnect", color = MindoListTheme.colors.error, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FooterNote() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MindoListTheme.colors.cardChildBg,
        border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            CheckCircleGlyph(color = MindoListTheme.colors.mint)
            Spacer(Modifier.width(10.dp))
            val text = buildAnnotatedString {
                append("Disconnecting stops future imports but ")
                withStyle(SpanStyle(color = MindoListTheme.colors.textPrimary, fontWeight = FontWeight.Bold)) {
                    append("keeps")
                }
                append(" any tasks already brought into TaskLedger.")
            }
            Text(text = text, color = MindoListTheme.colors.textSecondary, fontSize = 12.sp, lineHeight = 18.sp)
        }
    }
}


@Composable
private fun SyncGlyph(color: Color) {
    Canvas(modifier = Modifier.size(13.dp)) {
        drawArc(
            color = color,
            startAngle = -40f,
            sweepAngle = 260f,
            useCenter = false,
            style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
        )
        val arrow = Path().apply {
            moveTo(size.width * 0.62f, 0f)
            lineTo(size.width, size.height * 0.08f)
            lineTo(size.width * 0.78f, size.height * 0.38f)
            close()
        }
        drawPath(path = arrow, color = color)
    }
}

@Composable
private fun CheckCircleGlyph(color: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
        drawCircle(color = color, radius = size.minDimension / 2f, style = Stroke(width = 1.4.dp.toPx()))
        val check = Path().apply {
            moveTo(size.width * 0.28f, size.height * 0.52f)
            lineTo(size.width * 0.44f, size.height * 0.68f)
            lineTo(size.width * 0.74f, size.height * 0.32f)
        }
        drawPath(
            path = check,
            color = color,
            style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
private fun HorizontalDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MindoListTheme.colors.textSecondary.copy(alpha = 0.2f))
    )
}

@Composable
fun IntegrationsHeader(onBackClick: () -> Unit) {
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
            text = "Integrations",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MindoListTheme.colors.textPrimary
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(44.dp)) // To center the title
    }
}

@Composable
fun LinkAccountsIllustration(modifier: Modifier = Modifier) {
    val panelColor = MindoListTheme.colors.textPrimary.copy(alpha = 0.05f)
    val lineColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f)
    
    Box(modifier = modifier.size(180.dp), contentAlignment = Alignment.Center) {

        // Backdrop circle + dashed ring + floating accent dots
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val center = Offset(w / 2f, h / 2f)

            drawCircle(color = panelColor, radius = w * 0.41f, center = center)
            drawCircle(
                color = lineColor,
                radius = w * 0.29f,
                center = center,
                style = Stroke(
                    width = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()))
                )
            )
            drawCircle(color = MindoListAccentFixed.copy(alpha = 0.7f), radius = 3.5.dp.toPx(), center = Offset(w * 0.24f, h * 0.3f))
            drawCircle(color = MindoListMintFixed.copy(alpha = 0.7f), radius = 3.dp.toPx(), center = Offset(w * 0.76f, h * 0.7f))
        }

        // Left square — Google logo
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MindoListTheme.colors.cardBg,
            border = BorderStroke(1.dp, lineColor),
            modifier = Modifier
                .size(46.dp)
                .offset(x = (-21).dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = GoogleLogo,
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Right square — checkmark, outlined in amber
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color.Transparent,
            border = BorderStroke(2.5.dp, MindoListAccentFixed.copy(alpha = 0.9f)),
            modifier = Modifier
                .size(46.dp)
                .offset(x = 21.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(18.dp)) {
                    val path = Path().apply {
                        moveTo(size.width * 0.1f, size.height * 0.5f)
                        lineTo(size.width * 0.4f, size.height * 0.8f)
                        lineTo(size.width * 0.95f, size.height * 0.15f)
                    }
                    drawPath(
                        path = path,
                        color = MindoListMintFixed,
                        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
            }
        }

        // Connector dot sitting on the seam between the two squares
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(MindoListTheme.colors.background, CircleShape)
                .border(2.dp, MindoListAccentFixed, CircleShape)
        )
    }
}

@Composable
fun IntegrationFeatures() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FeatureChip("One unified list")
            FeatureChip("Read-only, always")
        }
        FeatureChip("Takes under a minute")
    }
}

@Composable
fun FeatureChip(text: String) {
    Surface(
        color = MindoListTheme.colors.textPrimary.copy(alpha = 0.05f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MindoListTheme.colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MindoListTheme.colors.mint,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = MindoListTheme.colors.textPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
