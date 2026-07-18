package com.jrprofessor.mindolist

import androidx.compose.ui.window.ComposeUIViewController
import com.jrprofessor.mindolist.di.initKoin
import com.jrprofessor.mindolist.domain.repository.AiTaskRepository

fun MainViewController(aiTaskRepository: AiTaskRepository) = ComposeUIViewController {
    initKoin(aiTaskRepository)
    App()
}
