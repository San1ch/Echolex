package com.example.echolex.ui.screens.MainScreen.LessonSettingsScreen.dialog

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.R
import com.example.echolex.core.domain.data.model.lesson.CardSelectionMode
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuViewModel
import com.example.echolex.ui.customDesign.AppBorderButton
import com.example.echolex.ui.customDesign.AppStandardDialogBackground
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont


@Composable
fun ChooseSelectionModeDialog(viewModel: LessonMenuViewModel) {
    AppStandardDialogBackground(onOverlayClick = {
        viewModel.dialogCenter.openCurrentCreatingStageDialog()
    }) {
        val buttonSpacing = 10.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.choose_a_mode),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = nunitoVariableFont,
                    fontWeight = FontWeight.Black,
                    color = AppContentColor
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(buttonSpacing))

            AppBorderButton(
                text = stringResource(R.string.random),
                onClick = {
                    viewModel.chooseSelectionMode(CardSelectionMode.RANDOM)
                }
            )

            Spacer(modifier = Modifier.height(buttonSpacing))

            AppBorderButton(
                text = stringResource(R.string.prefer_low_priority),
                onClick = {
                    viewModel.chooseSelectionMode(CardSelectionMode.PREFER_LOW_PRIORITY)
                }
            )

            Spacer(modifier = Modifier.height(buttonSpacing))

            AppBorderButton(
                text = stringResource(R.string.prefer_high_priority),
                onClick = {
                    viewModel.chooseSelectionMode(CardSelectionMode.PREFER_HIGH_PRIORITY)
                }
            )

            Spacer(modifier = Modifier.height(buttonSpacing))

            AppBorderButton(
                text = stringResource(R.string.lock_to_priority),
                onClick = {
                    viewModel.chooseSelectionMode(CardSelectionMode.LOCK_TO_PRIORITY)
                }
            )
        }
    }
}
