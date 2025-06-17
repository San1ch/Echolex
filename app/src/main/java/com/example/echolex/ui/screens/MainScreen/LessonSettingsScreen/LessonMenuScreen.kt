package com.example.echolex.ui.screens.MainScreen.LessonSettingsScreen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuScreenViewModel
import com.example.echolex.ui.customDesign.StandardStart

@Composable
fun LessonScreen(viewModel: LessonMenuScreenViewModel = hiltViewModel()) {
    LessonMenuContent()
}

@Composable
fun LessonMenuContent() {
    StandardStart() {

    }
}