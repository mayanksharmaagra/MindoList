package com.jrprofessor.mindolist.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.R
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.theme.btnColor

// Colors
// Colors
private val CardBackground = Color(0xFFF5F6FA)
private val PurpleAccent = Color(0xFF6C3FC7)
private val TitleColor = Color(0xFF3A3A5C)
private val PlaceholderColor = Color(0xFFB0B3C6)

@Composable
fun AddTaskScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // ← add this
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        CustomToolBar()
        Spacer(modifier = Modifier.height(15.dp))
        IdentityView()
        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
fun IdentityView() {
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 32.dp)
        ) {
            // Header: icon + label
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_container),
                    contentDescription = "Icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Identity",
                    color = TitleColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title TextField
            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = {
                    Text(
                        text = "What needs to be done?",
                        color = TitleColor.copy(alpha = 0.45f),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                textStyle = TextStyle(
                    color = TitleColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = PurpleAccent
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3
            )

            // Details TextField
            TextField(
                value = details,
                onValueChange = { details = it },
                placeholder = {
                    Text(
                        text = "Add more details...",
                        color = PlaceholderColor,
                        fontSize = 15.sp
                    )
                },
                textStyle = TextStyle(
                    color = TitleColor,
                    fontSize = 15.sp
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = PurpleAccent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp),
                singleLine = false,
                maxLines = 5
            )
        }
    }
}

@Composable
fun CustomToolBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(R.drawable.ic_close), contentDescription = "Close")
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "New Task",
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_condensed_bold,
                    FontWeight.Normal
                )
            ),
            fontSize = 20.sp,
            color = Color(0xFF000000)
        )
        Spacer(modifier = Modifier.weight(1f))
        ActionButton(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(30.dp),
            text = "Create",
            fontSize = 14.sp,
            textColor = Color.White,
            containerColor = btnColor,
            isIconVisible = false,
            padding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
        ) {

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddTaskPreview() {
    AddTaskScreen()
}