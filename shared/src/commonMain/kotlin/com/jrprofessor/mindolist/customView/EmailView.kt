package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =====================================================
// EMAIL VIEW
// =====================================================

@Composable
fun ShowEmailView(
    email: String,
    emailError: String?,
    label: String = "Email",
    labelColor: Color = Color.Black,
    textColor: Color = Color.Black,
    containerColor: Color = Color.Transparent,
    unfocusedBorderColor: Color = Color(0xFFD1D5DB),
    focusedBorderColor: Color = Color(0xFF6366F1),
    placeholderColor: Color = Color(0xFF9CA3AF),
    onEmailChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        InputLabel(text = label, textColor = labelColor)
//
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("example@example.com", color = placeholderColor) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unfocusedBorderColor,
                errorBorderColor = Color(0xFFDC2626),
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor
            ),
            textStyle = LocalTextStyle.current.copy(
                color = textColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            isError = emailError != null
        )
        if (emailError != null) {
            Text(
                text = emailError,
                color = Color(0xFFDC2626),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}