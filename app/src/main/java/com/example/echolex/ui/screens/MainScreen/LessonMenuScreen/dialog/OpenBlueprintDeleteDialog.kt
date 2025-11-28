package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.echolex.R
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuViewModel

@Composable
fun OpenBlueprintDeleteDialog(viewModel: LessonMenuViewModel) {
    StandardYesNoDialog(
        labelText = stringResource(R.string.delete_blueprint_question),
        leftButtonText = stringResource(R.string.no),
        rightButtonText = stringResource(R.string.yes),
        onLeftClick = {
            viewModel.dialogCenter.closeDialog()
        },
        onRightClick = {
            viewModel.deleteBlueprint()
        }
    )
}
