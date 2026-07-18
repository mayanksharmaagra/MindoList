package com.jrprofessor.mindolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.screen.AnalyticsScreen
import com.jrprofessor.mindolist.theme.AppTheme
import com.jrprofessor.mindolist.viewmodels.AnalyticsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import org.koin.compose.KoinApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        // ✅ Install splash screen FIRST
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // ✅ Optional: Customize splash screen
        splashScreen.setKeepOnScreenCondition {
            // Return true to keep splash screen longer
            false
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    KoinApplication(application = {
        modules(module {
            single<TaskRepository> {
                object : TaskRepository {
                    override fun getTasks(): Flow<com.jrprofessor.mindolist.domain.model.Result<List<TaskModel>>> =
                        flowOf(com.jrprofessor.mindolist.domain.model.Result.Success(emptyList()))

                    override fun getTasksByDate(date: LocalDate): Flow<com.jrprofessor.mindolist.domain.model.Result<List<TaskModel>>> =
                        flowOf(com.jrprofessor.mindolist.domain.model.Result.Success(emptyList()))

                    override suspend fun addTask(task: TaskModel): com.jrprofessor.mindolist.domain.model.Result<Unit> =
                        com.jrprofessor.mindolist.domain.model.Result.Success(Unit)

                    override suspend fun updateTask(task: TaskModel): com.jrprofessor.mindolist.domain.model.Result<Unit> =
                        com.jrprofessor.mindolist.domain.model.Result.Success(Unit)

                    override suspend fun markComplete(taskId: String, isCompleted: Boolean): com.jrprofessor.mindolist.domain.model.Result<Unit> =
                        com.jrprofessor.mindolist.domain.model.Result.Success(Unit)

                    override suspend fun deleteTask(taskId: String): com.jrprofessor.mindolist.domain.model.Result<Unit> =
                        com.jrprofessor.mindolist.domain.model.Result.Success(Unit)
                }
            }
            viewModel { AnalyticsViewModel(get()) }
        })
    }) {
        AppTheme {
            AnalyticsScreen()
        }
    }
}
