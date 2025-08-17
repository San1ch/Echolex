package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog

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
import com.example.echolex.R
import com.example.echolex.core.domain.data.model.lesson.StageType
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuViewModel
import com.example.echolex.ui.customDesign.AppStandardDialogBackground
import com.example.echolex.ui.customDesign.DualButtonRow
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun ChooseStageModeDialog(viewModel: LessonMenuViewModel) {
    AppStandardDialogBackground {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.choose_stage),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppContentColor
                )
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            DualButtonRow(
                leftButtonText = stringResource(R.string.learn),
                rightButtonText = stringResource(R.string.repeat),
                onLeftClick = {
                    viewModel.addStage(StageType.LEARNING)
                },
                onRightClick = {
                    viewModel.addStage(StageType.REPEATING)
                }
            )
        }
    }
}
