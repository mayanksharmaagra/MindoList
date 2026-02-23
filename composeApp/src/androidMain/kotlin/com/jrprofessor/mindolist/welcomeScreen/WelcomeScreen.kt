package com.jrprofessor.mindolist.welcomeScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.R
import com.jrprofessor.mindolist.theme.btnColor
import com.jrprofessor.mindolist.utils.StatusBarInDarkMode
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit,
    onLoginClick: () -> Unit
) {

    StatusBarInDarkMode()
    // Animation state
    var animateIn by remember { mutableStateOf(false) }

    // Feature rotation
    var currentFeature by remember { mutableStateOf(0) }

    val features = listOf(
        Feature(Icons.Default.CheckCircle, "Organize tasks effortlessly"),
        Feature(Icons.Default.Notifications, "Never miss important moments"),
        Feature(Icons.Default.TrendingUp, "Track your productivity growth")
    )

    LaunchedEffect(Unit) {
        animateIn = true
        while (true) {
            delay(3000)
            currentFeature = (currentFeature + 1) % features.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
//        AnimatedVisibility(
//            visible = animateIn,
//            enter = fadeIn(animationSpec = tween(1000)) +
//                    slideInVertically(
//                        initialOffsetY = { it / 8 },
//                        animationSpec = tween(1000)
//                    )
//        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Top section - Logo & Branding
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo with rings
                LogoWithRings(animateIn)

                Spacer(modifier = Modifier.height(20.dp))

                // App name
                Text(
                    text = "MindoList",
                    fontSize = 56.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.roboto_condensed_extra_bold,
                            FontWeight.Bold
                        )
                    ),
                    style = LocalTextStyle.current.copy(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF3B82F6),
                                Color(0xFF3B82F6),
                                Color(0xFF3B82F6)
                            )
                        )
                    )
                )

                /*Spacer(modifier = Modifier.height(8.dp))

                SmartProductivityBadge()*/

                Spacer(modifier = Modifier.height(16.dp))

                // Tagline
                Text(
                    text = buildAnnotatedString {
                        append("Plan smart. Get reminded.\n")
                        withStyle(
                            style = SpanStyle(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF16A34A),
                                        Color(0xFF059669)
                                    )
                                )
                            )
                        ) {
                            append("Stay productive.")
                        }
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155),
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.roboto_condensed_regular,
                            FontWeight.Normal
                        )
                    )
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Features Preview
                FeaturesPreview()

            }

            // Bottom section - CTA
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Spacer(modifier = Modifier.height(32.dp))

                GetStartedButton(onClick = onGetStartedClick)

                Spacer(modifier = Modifier.height(24.dp))

                // Sign in link
                SignInLink(onClick = onLoginClick)

                Spacer(modifier = Modifier.height(16.dp))

                // Trust badges
                TrustBadges()
            }
        }
//        }
    }
}

@Composable
fun LogoWithRings(animateIn: Boolean) {
    val scale by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(160.dp)
    ) {
        // Outer ring
        Box(
            modifier = Modifier
                .size(144.dp)
                .border(
                    2.dp,
                    Color(0xFF9333EA).copy(alpha = 0.2f),
                    CircleShape
                )
        )

        // Inner ring
        Box(
            modifier = Modifier
                .size(128.dp)
                .border(
                    2.dp,
                    Color(0xFF6366F1).copy(alpha = 0.1f),
                    CircleShape
                )
        )

        // Logo circle
        Box(
            modifier = Modifier
                .size(112.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .shadow(16.dp, CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF9333EA),
                            Color(0xFF6366F1),
                            Color(0xFF3B82F6)
                        )
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Checklist icon
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
        }

        // Bell badge - Outside logo circle for proper positioning
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)  // Align to top-end of parent Box
                .offset(x = (-20).dp, y = 20.dp)  // Fine-tune position
                .size(32.dp)
                .shadow(8.dp, CircleShape)
                .background(Color.White, CircleShape)  // White border effect
                .padding(2.dp)  // Space for white border
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF4ADE80),
                            Color(0xFF10B981)
                        )
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.size(16.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun GetStartedButton(onClick: () -> Unit) {
    // Get Started Button
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0xFF9333EA).copy(alpha = 0.3f),
                spotColor = Color(0xFF9333EA).copy(alpha = 0.4f)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    btnColor
                    /*Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF9333EA),
                            Color(0xFF6366F1),
                            Color(0xFF3B82F6)
                        )
                    )*/
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun SmartProductivityBadge() {
    // Smart Productivity badge
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color(0xFF9333EA)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Smart Productivity",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF9333EA),
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_condensed_regular,
                    FontWeight.Normal
                )
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color(0xFF9333EA)
        )
    }
}

@Composable
fun TrustBadges() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TrustBadge(Icons.Default.CheckCircle, "Secure", Color(0xFF22C55E))

        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(4.dp)
                .background(Color(0xFFCBD5E1), CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        TrustBadge(Icons.Default.Lock, "Private", Color(0xFF3B82F6))

        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(4.dp)
                .background(Color(0xFFCBD5E1), CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        // Ad-free
        TrustBadge(
            icon = Icons.Default.Email,
            text = "Ad-free",
            iconColor = Color(0xFF3B82F6)
        )
    }
}

@Composable
fun TrustBadge(icon: ImageVector, text: String, iconColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_condensed_regular,
                    FontWeight.Normal
                )
            ),
            color = Color(0xFF64748B)
        )
    }
}

@Composable
fun SignInLink(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Already have an account?",
            fontSize = 14.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_condensed_regular,
                    FontWeight.Normal
                )
            ),
            color = Color(0xFF64748B)
        )
        Spacer(modifier = Modifier.width(4.dp))
        TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Sign In",
                fontSize = 14.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.roboto_condensed_regular,
                        FontWeight.Normal
                    )
                ),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF9333EA)
            )
        }
    }
}

@Composable
fun FeaturesPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Smart Tasks
        FeatureCard(
            icon = Icons.Default.CheckCircle,
            label = "Smart Tasks",
            backgroundColor = Color.White.copy(alpha = 0.5f),
            borderColor = Color(0xFFE9D5FF),
            iconBackgroundStart = Color(0xFFF3E8FF),
            iconBackgroundEnd = Color(0xFFDDD6FE),
            iconColor = Color(0xFF9333EA),
            modifier = Modifier.weight(1f)
        )

        // Reminders
        FeatureCard(
            icon = Icons.Default.Notifications,
            label = "Reminders",
            backgroundColor = Color.White.copy(alpha = 0.5f),
            borderColor = Color(0xFFC7D2FE),
            iconBackgroundStart = Color(0xFFE0E7FF),
            iconBackgroundEnd = Color(0xFFC7D2FE),
            iconColor = Color(0xFF6366F1),
            modifier = Modifier.weight(1f)
        )

        // Progress
        FeatureCard(
            icon = Icons.Default.CheckCircle,
            label = "Progress",
            backgroundColor = Color.White.copy(alpha = 0.5f),
            borderColor = Color(0xFFBBF7D0),
            iconBackgroundStart = Color(0xFFDCFCE7),
            iconBackgroundEnd = Color(0xFFBBF7D0),
            iconColor = Color(0xFF16A34A),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FeatureCard(
    icon: ImageVector,
    label: String,
    backgroundColor: Color,
    borderColor: Color,
    iconBackgroundStart: Color,
    iconBackgroundEnd: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    var isHovered by remember { mutableStateOf(false) }
    val borderColorAnimated by animateColorAsState(
        targetValue = if (isHovered) iconColor.copy(alpha = 0.3f) else borderColor,
        animationSpec = tween(300)
    )
    val elevation by animateDpAsState(
        targetValue = if (isHovered) 12.dp else 0.dp,
        animationSpec = tween(300)
    )

    Column(
        modifier = modifier
            .shadow(elevation, RoundedCornerShape(16.dp))
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .border(1.dp, borderColorAnimated, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Icon background
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(iconBackgroundStart, iconBackgroundEnd)
                    ),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = iconColor
            )
        }

        // Label
        Text(
            text = label,
            fontSize = 12.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_condensed_regular,
                    FontWeight.SemiBold
                )
            ),
            color = Color(0xFF334155),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    WelcomeScreen({}, {})
}