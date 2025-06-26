package com.example.echolex.ui.screens.MainScreen.LessonSettingsScreen.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.echolex.R
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuViewModel
import com.example.echolex.ui.customDesign.AppStandardDialogBackground
import com.example.echolex.ui.customDesign.DualButtonRow
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun DeleteStageInCreateDialog(viewModel: LessonMenuViewModel) {
    StandardYesNoDialog(
        labelText = stringResource(R.string.do_you_want_to_delete_the_stage),
        leftButtonText = stringResource(R.string.no),
        rightButtonText = stringResource(R.string.yes),
        onLeftClick = {
            viewModel.dialogCenter.openCreateBlueprintDialog()
        },
        onRightClick = {
            viewModel.deleteStage()
        }
    )
}

@Composable
fun StandardYesNoDialog(
    labelText: String,
    leftButtonText: String,
    rightButtonText: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit
) {
    AppStandardDialogBackground {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), contentAlignment = Alignment.Center
            ) {
                Text(
                    labelText, style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = nunitoVariableFont,
                        color = AppContentColor
                    )
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            DualButtonRow(
                onLeftClick = onLeftClick, onRightClick = onRightClick,
                leftButtonText = leftButtonText,
                rightButtonText = rightButtonText
            )
        }
    }
}
