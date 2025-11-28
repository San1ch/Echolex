package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.R
import com.example.echolex.core.domain.data.model.lesson.CardSelectionMode
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuViewModel
import com.example.echolex.ui.customDesign.AppStandardDialogBackground
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun ChooseSelectionModeInfoDialog(viewModel: LessonMenuViewModel) {
    AppStandardDialogBackground(onOverlayClick = {
        viewModel.dialogCenter.openCreateBlueprintStageDialog()
    }) {
        val currentStage = viewModel.uiState.value.currentCreatingStage
        val currentMode = currentStage.cardSelectionMode
        val text = when (currentMode) {
            CardSelectionMode.Random -> {
                stringResource(R.string.random_mode_info)
            }

            CardSelectionMode.PreferLowPriority -> {
                stringResource(R.string.prefer_low_priority_mode_info)
            }

            CardSelectionMode.PreferHighPriority -> {
                stringResource(R.string.prefer_high_priority_mode_info)
            }

            CardSelectionMode.LockToPriority -> {
                stringResource(R.string.lock_to_priority_mode_info)
            }
            else -> {
                stringResource(R.string.random_mode_info)
            }
        }
        ChooseSelectionModeInfoDialogContent(viewModel, text)
    }
}

@Composable
private fun ChooseSelectionModeInfoDialogContent(viewModel: LessonMenuViewModel, text: String) {
    Box(modifier = Modifier
    .fillMaxWidth().wrapContentHeight().padding(30.dp), contentAlignment = Alignment.Center) {
        Text(
            text, style = TextStyle(
                fontSize = 20.sp,
                fontFamily = nunitoVariableFont,
                color = AppContentColor
            )
        )
    }
}
