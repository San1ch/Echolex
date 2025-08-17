package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.R
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuViewModel
import com.example.echolex.ui.customDesign.AppCheckbox
import com.example.echolex.ui.customDesign.AppOutlinedTextField
import com.example.echolex.ui.customDesign.AppStandardDialogBackground
import com.example.echolex.ui.customDesign.AppStandardLabel
import com.example.echolex.ui.customDesign.DualButtonRow
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.item.StageCreatingButton
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.item.StageItemCreating
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun CreateBlueprintDialog(viewModel: LessonMenuViewModel) {
    val currentBlueprint = viewModel.uiState.value.currentCreatingBlueprint
    val lessonStageList = currentBlueprint.stages

    AppStandardDialogBackground {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(20.dp)
        ) {
            AppStandardLabel(stringResource(R.string.stage_chain))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .border(1.dp, AppContentColor, RoundedCornerShape(10.dp))
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        items(1) {
                            StageCreatingButton(
                                modifier = Modifier
                                    .size(50.dp),
                                onClick = {
                                    viewModel.dialogCenter.openChooseStageModeDialog()
                                })
                        }
                        items(lessonStageList.size) {
                            StageItemCreating(
                                modifier = Modifier
                                    .size(50.dp),
                                stage = lessonStageList[it],
                                onClick = {
                                    viewModel.openDeleteStageDialog(lessonStageList[it])
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier
                .height(10.dp))
            Column {
                Box(
                    modifier = Modifier
                ) {
                    AppStandardLabel(
                        text = stringResource(R.string.blueprint_name),
                    )
                }

                AppOutlinedTextField(
                    value = currentBlueprint.name,
                    onValueChange = { viewModel.updateCurrentCreatingBlueprint(name = it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 20.dp, top = 16.dp)
            ) {
                AppCheckbox(
                    checked = currentBlueprint.settings.isLoop,
                    onCheckedChange = { viewModel.updateCurrentCreatingBlueprint(isLoop = it) },
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.lesson_loop),
                    style = TextStyle(
                        fontFamily = nunitoVariableFont,
                        fontSize = 16.sp,
                        color = AppContentColor
                    )
                )
            }
            DualButtonRow(
                leftButtonText = stringResource(R.string.cancel),
                rightButtonText = stringResource(R.string.create),
                onLeftClick = { viewModel.dialogCenter.closeDialog() },
                onRightClick = {
                    viewModel.createBlueprint()
                }
            )
        }
    }
}
