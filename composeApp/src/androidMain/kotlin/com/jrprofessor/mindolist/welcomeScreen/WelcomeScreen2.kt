
package com.jrprofessor.mindolist.welcomeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Preview
@Composable
fun WelcomeSplashScreen2Preview() {
    WelcomeSplashScreen2()
}

@Composable

fun WelcomeSplashScreen2(
    onGetStarted: () -> Unit = {},
    onSignIn: () -> Unit = {}
) {
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
        // Top wave decoration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF9333EA).copy(alpha = 0.05f),
                            Color(0xFF6366F1).copy(alpha = 0.05f),
                            Color(0xFF3B82F6).copy(alpha = 0.05f)
                        )
                    )
                )
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section - Logo & Branding
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo with rings
                LogoWithRings(animateIn)

                Spacer(modifier = Modifier.height(40.dp))

                // App name
                Text(
                    text = "MindoList",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    style = LocalTextStyle.current.copy(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF9333EA),
                                Color(0xFF6366F1),
                                Color(0xFF3B82F6)
                            )
                        )
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

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
                        color = Color(0xFF9333EA)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF9333EA)
                    )
                }

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
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Rotating feature
                RotatingFeatureCard(features[currentFeature], animateIn)

                Spacer(modifier = Modifier.height(24.dp))

                // Feature dots
                FeatureDots(currentFeature, features.size)
            }

            // Bottom section - CTA
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Stats grid
                StatsGrid()

                Spacer(modifier = Modifier.height(32.dp))

                // Get Started button
                GetStartedButton(onClick = onGetStarted)

                Spacer(modifier = Modifier.height(24.dp))

                // Sign in link
                SignInLink(onClick = onSignIn)

                Spacer(modifier = Modifier.height(16.dp))

                // Trust badges
                TrustBadges()
            }
        }
    }
}



@Composable
fun RotatingFeatureCard(feature: Feature, visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFF8FAFC),
                            Color(0xFFF5F3FF)
                        )
                    ),
                    RoundedCornerShape(16.dp)
                )
                .border(
                    1.dp,
                    Color(0xFF9333EA).copy(alpha = 0.1f),
                    RoundedCornerShape(16.dp)
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF9333EA),
                                Color(0xFF6366F1)
                            )
                        ),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Text
            Text(
                text = feature.text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF334155)
            )
        }
    }
}

@Composable
fun FeatureDots(currentIndex: Int, totalCount: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(totalCount) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(6.dp)
                    .width(if (index == currentIndex) 32.dp else 6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        if (index == currentIndex) {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF9333EA),
                                    Color(0xFF6366F1)
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFCBD5E1),
                                    Color(0xFFCBD5E1)
                                )
                            )
                        }
                    )
                    .animateContentSize()
            )
        }
    }
}

@Composable
fun StatsGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            value = "500K+",
            label = "Users",
            backgroundColor = Color(0xFFF5F3FF),
            borderColor = Color(0xFF9333EA).copy(alpha = 0.1f),
            valueColor = Color(0xFF9333EA),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = "4.8★",
            label = "Rating",
            backgroundColor = Color(0xFFEFF6FF),
            borderColor = Color(0xFF3B82F6).copy(alpha = 0.1f),
            valueColor = Color(0xFF3B82F6),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = "Free",
            label = "Forever",
            backgroundColor = Color(0xFFF0FDF4),
            borderColor = Color(0xFF22C55E).copy(alpha = 0.1f),
            valueColor = Color(0xFF22C55E),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    backgroundColor: Color,
    borderColor: Color,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF64748B)
        )
    }
}

// Data class
data class Feature(
    val icon: ImageVector,
    val text: String
)