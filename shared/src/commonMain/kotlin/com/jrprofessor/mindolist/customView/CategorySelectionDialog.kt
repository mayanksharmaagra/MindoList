package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jrprofessor.mindolist.model.Category
import com.jrprofessor.mindolist.theme.*

@Composable
fun CategorySelectionDialog(
    selectedCategory: Category,
    categoryCounts: Map<String, Int> = emptyMap(),
    onCategorySelected: (Category) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var currentSelection by remember { mutableStateOf(selectedCategory) }

    val filteredCategories = Category.entries.filter {
        it.label.contains(searchQuery, ignoreCase = true)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MindoListTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // ── Header ────────────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Category",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MindoListTheme.colors.textPrimary,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        onClick = onDismiss,
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MindoListTheme.colors.cardChildBg,
                        border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MindoListTheme.colors.textPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Search Bar ────────────────────────────────────────────────
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search categories", color = MindoListTheme.colors.textSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = MindoListTheme.colors.textSecondary)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                        unfocusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                        focusedContainerColor = MindoListTheme.colors.inputBg,
                        unfocusedContainerColor = MindoListTheme.colors.inputBg,
                        focusedTextColor = MindoListTheme.colors.textPrimary,
                        unfocusedTextColor = MindoListTheme.colors.textPrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ── Category List ─────────────────────────────────────────────
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredCategories) { category ->
                        CategoryListItem(
                            category = category,
                            isSelected = category == currentSelection,
                            count = categoryCounts[category.label] ?: 0,
                            onClick = { currentSelection = category }
                        )
                    }

//                    item {
//                        AddNewCategoryPlaceholder()
//                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Footer Button ─────────────────────────────────────────────
                Button(
                    onClick = {
                        onCategorySelected(currentSelection)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MindoListTheme.colors.textPrimary,
                        contentColor = MindoListTheme.colors.background
                    )
                ) {
                    Text(
                        text = "Use \"${currentSelection.label}\"",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryListItem(
    category: Category,
    isSelected: Boolean,
    count: Int,
    onClick: () -> Unit
) {
    val border = if (isSelected) BorderStroke(1.dp, MindoListTheme.colors.accent) else null
    val bgColor = if (isSelected) MindoListTheme.colors.accent.copy(alpha = 0.1f) else MindoListTheme.colors.cardBg

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        border = border
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Box
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = MindoListTheme.colors.cardChildBg
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = category.iconRes,
                        contentDescription = null,
                        tint = category.iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.label,
                    color = MindoListTheme.colors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$count tasks", 
                    color = MindoListTheme.colors.textSecondary,
                    fontSize = 14.sp
                )
            }

            // Custom Radio Circle
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) MindoListTheme.colors.accent else MindoListTheme.colors.textSecondary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(MindoListTheme.colors.accent)
                    )
                }
            }
        }
    }
}

@Composable
fun AddNewCategoryPlaceholder() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MindoListTheme.colors.accent,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Add new category",
                color = MindoListTheme.colors.accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
