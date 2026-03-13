package com.jrprofessor.mindolist.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.jrprofessor.mindolist.icons.IcCreative
import com.jrprofessor.mindolist.icons.IcEducation
import com.jrprofessor.mindolist.icons.IcFinance
import com.jrprofessor.mindolist.icons.IcHealth
import com.jrprofessor.mindolist.icons.IcHome
import com.jrprofessor.mindolist.icons.IcPersonal
import com.jrprofessor.mindolist.icons.IcShopping
import com.jrprofessor.mindolist.icons.IcWork
enum class Category(
    val label: String,
    val dbKey: String,           // saved in Firebase Realtime DB
    val iconRes: ImageVector,            // Res.drawable.Ic*  — replace with your actual drawable names
) {
    WORK(
        label = "Work",
        dbKey = "work",
        iconRes = IcWork,
    ),
    PERSONAL(
        label = "Personal",
        dbKey = "personal",
        iconRes = IcPersonal,
    ),
    SHOPPING(
        label = "Shopping",
        dbKey = "shopping",
        iconRes = IcShopping,
    ),
    HEALTH(
        label = "Health",
        dbKey = "health",
        iconRes = IcHealth,
    ),
    EDUCATION(
        label = "Education",
        dbKey = "education",
        iconRes = IcEducation,
    ),
    FINANCE(
        label = "Finance",
        dbKey = "finance",
        iconRes = IcFinance,
    ),
    SOCIAL(
        label = "Social",
        dbKey = "social",
        iconRes = IcShopping,
    ),
    HOME(
        label = "Home",
        dbKey = "home",
        iconRes = IcHome,
    ),
    CREATIVE(
        label = "Creative",
        dbKey = "creative",
        iconRes = IcCreative,
    );

    companion object {
        fun fromDbKey(key: String): Category = entries.firstOrNull { it.dbKey == key } ?: PERSONAL
    }
}
