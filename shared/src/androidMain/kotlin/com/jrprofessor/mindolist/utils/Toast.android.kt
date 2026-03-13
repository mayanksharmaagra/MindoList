package com.jrprofessor.mindolist.utils

import android.content.Context
import android.widget.Toast
import org.koin.java.KoinJavaComponent.getKoin

actual fun showToast(message: String) {
    val context = getKoin().get<Context>()
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}