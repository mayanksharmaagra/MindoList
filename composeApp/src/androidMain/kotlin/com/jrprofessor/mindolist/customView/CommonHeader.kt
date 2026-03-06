package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.R

// =====================================================
// HEADER COMPONENTS
// =====================================================
@Composable
fun SignUpHeader(toolbarTitle: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.iv_back),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
        Text(
            text = toolbarTitle,
            fontSize = 20.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_condensed_regular,
                    FontWeight.SemiBold
                )
            ),
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        // Balance space for centered title
        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
fun WelcomeText(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontFamily = FontFamily(
            Font(R.font.roboto_condensed_regular, FontWeight.Normal)
        ),
        color = Color(0xFF64748B),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}