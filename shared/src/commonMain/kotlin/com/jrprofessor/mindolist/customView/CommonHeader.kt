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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.icons.IvBack
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

// =====================================================
// HEADER COMPONENTS
// =====================================================
@Composable
fun SignUpHeader(
    toolbarTitle: String,
    onBackClick: () -> Unit,
    textColor: Color = Color.Black,
    iconTint: Color = Color.Black
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = IvBack,
                contentDescription = "Back",
                modifier = Modifier.size(24.dp),
                tint = iconTint
            )
        }
        Text(
            text = toolbarTitle,
            fontSize = 20.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        // Balance space for centered title
        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
fun WelcomeText(title: String,textColor: Color=Color(0xFF64748B)) {
    Text(
        text = title,
        fontSize = 16.sp,
        /*fontFamily = FontFamily(
            Font(Res.font.roboto_condensed_regular, FontWeight.Normal)
        ),*/
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}