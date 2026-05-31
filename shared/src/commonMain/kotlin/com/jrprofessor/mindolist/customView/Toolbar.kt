package com.jrprofessor.mindolist.customView

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.theme.PrimaryBlue
import com.jrprofessor.mindolist.theme.backgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(name:String,onBackClick: () -> Unit={}) {
    TopAppBar(
        title = {
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color =  Color.Black,
            )
        },
        navigationIcon = {
//            IconButton(onClick = onBackClick) {
//                androidx.compose.material3.Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = "Back",
//                    tint = Color.Black,
//                )
//            }
        },
        actions = {
//            IconButton(onClick = {}) {
//                Icon(
//                    imageVector = Icons.Default.MoreVert,
//                    contentDescription = "More",
//                    tint = PrimaryBlue,
//                )
//            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
        ),
    )
}