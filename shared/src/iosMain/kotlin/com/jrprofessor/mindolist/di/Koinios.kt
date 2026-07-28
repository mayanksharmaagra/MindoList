package com.jrprofessor.mindolist.di

import com.jrprofessor.mindolist.domain.repository.AiTaskRepository
import org.koin.core.context.startKoin
import org.koin.dsl.module

object KoinHelper {
    fun doInitKoin(aiTaskRepository: AiTaskRepository) {
        startKoin {
            modules(
                appModules() + module {
                    single { aiTaskRepository }
                }
            )
        }
    }
}
