package com.jrprofessor.mindolist.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jrprofessor.mindolist.icons.IcCreative
import com.jrprofessor.mindolist.icons.IcEducation
import com.jrprofessor.mindolist.icons.IcFinance
import com.jrprofessor.mindolist.icons.IcHealth
import com.jrprofessor.mindolist.icons.IcHome
import com.jrprofessor.mindolist.icons.IcPersonal
import com.jrprofessor.mindolist.icons.IcShopping
import com.jrprofessor.mindolist.icons.IcSocial
import com.jrprofessor.mindolist.icons.IcWork
enum class Category(
    val label: String,
    val dbKey: String,
    val iconRes: ImageVector,
    val iconColor: Color,         // icon ka color
    val iconBgColor: Color,       // icon ke peeche circle bg
    val cardColor: Color,         // poore card ka bg
) {
    WORK(
        label = "Work",
        dbKey = "work",
        iconRes = IcWork,
        iconColor = Color(0xFF7C3AED),      // purple
        iconBgColor = Color(0xFFDDD6FE),    // light purple circle
        cardColor = Color(0xFFF3F0FF),      // very light purple card
    ),
    PERSONAL(
        label = "Personal",
        dbKey = "personal",
        iconRes = IcPersonal,
        iconColor = Color(0xFF3B82F6),      // blue
        iconBgColor = Color(0xFFBFDBFE),    // light blue circle
        cardColor = Color(0xFFEFF6FF),      // very light blue card
    ),
    SHOPPING(
        label = "Shopping",
        dbKey = "shopping",
        iconRes = IcShopping,
        iconColor = Color(0xFF059669),      // green
        iconBgColor = Color(0xFFA7F3D0),    // light green circle
        cardColor = Color(0xFFECFDF5),      // very light green card
    ),
    HEALTH(
        label = "Health",
        dbKey = "health",
        iconRes = IcHealth,
        iconColor = Color(0xFFE11D48),      // red
        iconBgColor = Color(0xFFFECACA),    // light red circle
        cardColor = Color(0xFFFFF1F2),      // very light red card
    ),
    EDUCATION(
        label = "Education",
        dbKey = "education",
        iconRes = IcEducation,
        iconColor = Color(0xFF0891B2),      // teal
        iconBgColor = Color(0xFFA5F3FC),    // light teal circle
        cardColor = Color(0xFFECFEFF),      // very light teal card
    ),
    FINANCE(
        label = "Finance",
        dbKey = "finance",
        iconRes = IcFinance,
        iconColor = Color(0xFFD97706),      // amber
        iconBgColor = Color(0xFFFDE68A),    // light amber circle
        cardColor = Color(0xFFFFFBEB),      // very light amber card
    ),
    SOCIAL(
        label = "Social",
        dbKey = "social",
        iconRes = IcSocial,
        iconColor = Color(0xFFDB2777),      // pink
        iconBgColor = Color(0xFFFBCFE8),    // light pink circle
        cardColor = Color(0xFFFDF2F8),      // very light pink card
    ),
    HOME(
        label = "Home",
        dbKey = "home",
        iconRes = IcHome,
        iconColor = Color(0xFFEA580C),      // orange
        iconBgColor = Color(0xFFFED7AA),    // light orange circle
        cardColor = Color(0xFFFFF7ED),      // very light orange card
    ),
    CREATIVE(
        label = "Creative",
        dbKey = "creative",
        iconRes = IcCreative,
        iconColor = Color(0xFF4F46E5),      // indigo
        iconBgColor = Color(0xFFC7D2FE),    // light indigo circle
        cardColor = Color(0xFFEEF2FF),      // very light indigo card
    );

    companion object {
        fun fromDbKey(key: String): Category =
            entries.firstOrNull { it.dbKey == key } ?: PERSONAL
    }
}
