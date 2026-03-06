package com.jrprofessor.mindolist.customView

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(14.dp),
    containerColor: Color = Color(0xFF3B82F6),  // ← Direct color parameter
    text: String,
    fontSize: TextUnit = 18.sp,
    textColor: Color = Color.White,
    fontWeight: FontWeight = FontWeight.SemiBold,
    isIconVisible: Boolean = false,
    iconTint: Color = Color.White,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    loadingText: String = "Loading...",
    elevation: Dp = 5.dp,
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "button_scale"
    )

    val alpha by animateFloatAsState(
        targetValue = when {
            !isEnabled -> 0.5f
            isLoading -> 0.7f
            else -> 1f
        },
        animationSpec = tween(300),
        label = "button_alpha"
    )

    val buttonElevation by animateDpAsState(
        targetValue = if (isEnabled && !isLoading) elevation else elevation / 2,
        label = "button_elevation"
    )

    Button(
        onClick = {
            if (isEnabled && !isLoading) {
                isPressed = true
                onClick()
                scope.launch {
                    delay(200)
                    isPressed = false
                }
            }
        },
        enabled = isEnabled && !isLoading,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .shadow(
                elevation = buttonElevation,
                shape = shape,
                ambientColor = Color(0xFF333333).copy(alpha = 0.3f),
                spotColor = Color(0xFF333333).copy(alpha = 0.4f)
            ),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor  // ← Same color for disabled
        ),
        contentPadding = padding
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = textColor,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = loadingText,
                    fontSize = fontSize,
                    color = textColor,
                    fontWeight = fontWeight
                )
            } else {
                Text(
                    text = text,
                    fontSize = fontSize,
                    color = textColor,
                    fontWeight = fontWeight
                )

                if (isIconVisible) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Arrow Forward",
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}