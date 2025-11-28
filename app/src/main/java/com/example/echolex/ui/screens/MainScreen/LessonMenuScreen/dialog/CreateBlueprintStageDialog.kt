package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.R
import com.example.echolex.core.constants.MAX_COUNT_OF_CARDS_FOR_LEARNING
import com.example.echolex.core.constants.MAX_COUNT_OF_CARDS_FOR_REPEATING
import com.example.echolex.core.constants.MAX_COUNT_OF_CORRECT_CYCLES_FOR_LEARNING
import com.example.echolex.core.constants.MAX_COUNT_OF_CORRECT_CYCLES_FOR_REPEATING
import com.example.echolex.core.constants.MAX_COUNT_REPEATING_PRIORITY
import com.example.echolex.core.constants.MIN_COUNT_OF_CARDS_FOR_LEARNING
import com.example.echolex.core.constants.MIN_COUNT_OF_CARDS_FOR_REPEATING
import com.example.echolex.core.constants.MIN_COUNT_OF_CORRECT_CYCLES_FOR_LEARNING
import com.example.echolex.core.constants.MIN_COUNT_OF_CORRECT_CYCLES_FOR_REPEATING
import com.example.echolex.core.constants.MIN_COUNT_REPEATING_PRIORITY
import com.example.echolex.core.domain.data.model.lesson.CardSelectionMode
import com.example.echolex.core.domain.data.model.lesson.StageType
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuViewModel
import com.example.echolex.ui.customDesign.AppBorderButton
import com.example.echolex.ui.customDesign.AppSliderLabelCount
import com.example.echolex.ui.customDesign.AppStandardDialogBackground
import com.example.echolex.ui.customDesign.DualButtonRow
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont


@Composable
fun CreateBlueprintStageDialog(viewModel: LessonMenuViewModel) {
    AppStandardDialogBackground {
        when (viewModel.uiState.value.currentCreatingStage.type) {
            StageType.REPEATING -> {
                CreateBlueprintRepeatStage(viewModel)
            }

            StageType.LEARNING -> {
                CreateBlueprintLearnStage(viewModel)
            }
        }
    }
}

@Composable
private fun CreateBlueprintRepeatStage(viewModel: LessonMenuViewModel) {
    val stage = viewModel.uiState.value.currentCreatingStage

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(15.dp),
    ) {
        Text(
            stringResource(R.string.repeat_stage), style = TextStyle(
                fontSize = 20.sp, fontFamily = nunitoVariableFont, color = AppContentColor
            ), modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        CreateBlueprintStageContent(
            cards = stage.cards.toFloat(),
            cycles = stage.cycles.toFloat(),
            onCardsChange = { viewModel.updateStageCardsValue(it.toInt()) },
            onCyclesChange = { viewModel.updateCyclesValue(it.toInt()) },
            minCards = MIN_COUNT_OF_CARDS_FOR_REPEATING,
            maxCards = MAX_COUNT_OF_CARDS_FOR_REPEATING,
            minCycles = MIN_COUNT_OF_CORRECT_CYCLES_FOR_REPEATING,
            maxCycles = MAX_COUNT_OF_CORRECT_CYCLES_FOR_REPEATING,
        )

        if(stage.cardSelectionMode != CardSelectionMode.Random) {
            AppSliderLabelCount(
                text = stringResource(R.string.priority) + ": ",
                value = stage.priority.toFloat(),
                onValueChange = { viewModel.updatePriorityLevel(it.toInt()) },
                min = MIN_COUNT_REPEATING_PRIORITY.toFloat(),
                max = MAX_COUNT_REPEATING_PRIORITY.toFloat(),
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()){
            AppBorderButton(
                text = stage.cardSelectionMode.label,
                onClick = { viewModel.dialogCenter.openChooseSelectionModeDialog() },
                modifier = Modifier.width(200.dp),
                borderWidth = 1.dp,
                textSize = 15
            )
            Spacer(modifier = Modifier
                        .width(20.dp))
            IconButton(
                onClick = {
                    viewModel.dialogCenter.openSelectionModeInfoDialog()
                }
            ) {
                Icon(imageVector = Icons.Default.Info, contentDescription = stringResource(R.string.info), tint = AppContentColor)
            }
        }

        DualButtonRow(
            leftButtonText = stringResource(R.string.back),
            rightButtonText = stringResource(R.string.create),
            onLeftClick = { viewModel.dialogCenter.openChooseStageModeDialog() },
            onRightClick = { viewModel.createAndAddStage() })
    }
}

@Composable
private fun CreateBlueprintLearnStage(viewModel: LessonMenuViewModel) {
    val stage = viewModel.uiState.value.currentCreatingStage
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(15.dp),
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stringResource(R.string.learn_stage), style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppContentColor
                )
            )
        }
        CreateBlueprintStageContent(
            cards = stage.cards.toFloat(),
            cycles = stage.cycles.toFloat(),
            onCardsChange = { viewModel.updateStageCardsValue(it.toInt()) },
            onCyclesChange = { viewModel.updateCyclesValue(it.toInt()) },
            minCards = MIN_COUNT_OF_CARDS_FOR_LEARNING,
            maxCards = MAX_COUNT_OF_CARDS_FOR_LEARNING,
            minCycles = MIN_COUNT_OF_CORRECT_CYCLES_FOR_LEARNING,
            maxCycles = MAX_COUNT_OF_CORRECT_CYCLES_FOR_LEARNING,
        )

        DualButtonRow(
            leftButtonText = stringResource(R.string.back),
            rightButtonText = stringResource(R.string.create),
            onLeftClick = { viewModel.dialogCenter.openChooseStageModeDialog() },
            onRightClick = { viewModel.createAndAddStage() })
    }
}

@Composable
private fun CreateBlueprintStageContent(
    cards: Float,
    cycles: Float,
    onCardsChange: (Float) -> Unit,
    onCyclesChange: (Float) -> Unit,
    minCards: Int,
    maxCards: Int,
    minCycles: Int,
    maxCycles: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AppSliderLabelCount(
            text = stringResource(R.string.cards) + ": ",
            value = cards,
            onValueChange = { onCardsChange(it) },
            min = minCards.toFloat(),
            max = maxCards.toFloat()
        )
        AppSliderLabelCount(
            text = stringResource(R.string.cycles) + ": ",
            value = cycles,
            onValueChange = { onCyclesChange(it) },
            min = minCycles.toFloat(),
            max = maxCycles.toFloat()
        )
    }
}
