package com.jrprofessor.mindolist.di

import com.jrprofessor.mindolist.domain.repository.AiTaskRepository
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(aiTaskRepository: AiTaskRepository) {
    startKoin {
        modules(appModules() + module {
            single { aiTaskRepository }
        })
    }
}