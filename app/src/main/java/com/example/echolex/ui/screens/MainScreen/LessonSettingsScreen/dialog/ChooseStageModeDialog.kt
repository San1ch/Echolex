package com.example.echolex.ui.screens.MainScreen.LessonSettingsScreen.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.echolex.core.domain.data.model.lesson.LearningStage
import com.example.echolex.core.domain.data.model.lesson.LessonStage
import com.example.echolex.core.domain.data.model.lesson.RepeatingStage
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuViewModel
import com.example.echolex.ui.customDesign.AppBorderButton
import com.example.echolex.ui.customDesign.AppStandardDialogBackground
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont


@Composable
fun ChooseStageModeDialog(viewModel: LessonMenuViewModel) {
    AppStandardDialogBackground {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.choose_stage), style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppContentColor
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AppBorderButton(
                    text = stringResource(R.string.learn),
                    onClick = {
                        viewModel.dialogCenter.openCreateBlueprintStageDialog(
                            LearningStage()
                        )
                    },
                    modifier = Modifier
                        .size(100.dp)
                )

                AppBorderButton(
                    text = stringResource(R.string.repeat),
                    onClick = {
                        viewModel.dialogCenter.openCreateBlueprintStageDialog(
                            RepeatingStage()
                        )
                    },
                    modifier = Modifier
                        .size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppBorderButton(
                text = stringResource(R.string.cancel),
                onClick = {
                    viewModel.dialogCenter.openCreateBlueprintDialog()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )
        }
    }
}
