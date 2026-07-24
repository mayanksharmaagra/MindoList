package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun InputLabel(text: String,textColor: Color = Color.Black,fontWeight: FontWeight= FontWeight.Normal) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = textColor,
        modifier = Modifier.fillMaxWidth(),
        fontWeight=fontWeight,
        textAlign = TextAlign.Start
    )
}