package com.jrprofessor.mindolist

import androidx.compose.ui.window.ComposeUIViewController
import com.jrprofessor.mindolist.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}
