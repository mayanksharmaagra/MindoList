package com.jrprofessor.mindolist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.jrprofessor.mindolist.navGraph.AppNavigation
import com.jrprofessor.mindolist.theme.AppTheme
import com.jrprofessor.mindolist.theme.ThemeMode
import com.jrprofessor.mindolist.viewmodels.SettingsViewmodel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun App() {
    val settingsViewModel: SettingsViewmodel = koinViewModel()
    val settingsState by settingsViewModel.state.collectAsState()

    AppTheme(themeMode = settingsState.themeMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation()
        }
    }
}
