package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.echolex.R
import com.example.echolex.core.ui.dialog.LessonMenuDialogState
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuScreenUiState
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuUiState
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuViewModel
import com.example.echolex.ui.customDesign.StandardAppBackground
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog.CreateBlueprintDialog
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog.CreateLessonDialog
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog.DeleteLessonDialog
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog.OpenBlueprintDeleteDialog
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog.ChooseStageModeDialog
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog.ChooseSelectionModeDialog
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog.ChooseSelectionModeInfoDialog
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog.CreateBlueprintStageDialog
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog.DeleteStageInCreateDialog
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog.logHere
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.item.LessonItem
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.item.BlueprintItem
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.AppTransparencyColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun LessonScreen() {
    LessonMenuContent()
    DialogList()
}

@Composable
fun DialogList(viewModel: LessonMenuViewModel = hiltViewModel()) {
    val dialogState = viewModel.uiState.value.lessonMenuDialogUiState
    when (dialogState) {
        LessonMenuDialogState.Closed -> {}

        LessonMenuDialogState.CreateLesson -> {
            CreateLessonDialog(viewModel)
        }

        LessonMenuDialogState.CreateBlueprint -> {
            CreateBlueprintDialog(viewModel)
        }

        LessonMenuDialogState.ChooseStageMode -> {
            ChooseStageModeDialog(viewModel)
        }

        LessonMenuDialogState.DeleteStage -> {
            DeleteStageInCreateDialog(viewModel)
        }

        LessonMenuDialogState.DeleteBlueprint -> {
            OpenBlueprintDeleteDialog(viewModel)
        }

        LessonMenuDialogState.DeleteLesson -> {
            DeleteLessonDialog(viewModel)
        }

        LessonMenuDialogState.CreateBlueprintStage -> {
            CreateBlueprintStageDialog(viewModel)
        }

        LessonMenuDialogState.ChooseSelectionMode -> {
            ChooseSelectionModeDialog(viewModel)
        }

        LessonMenuDialogState.SelectionModeInfo -> {
            ChooseSelectionModeInfoDialog(viewModel)
        }

        LessonMenuDialogState.CurrentCreatingStage -> {
            //TODO()
        }
    }
}

@Composable
fun LessonMenuContent(viewModel: LessonMenuViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState

    StandardAppBackground()
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            ScreenTopBar()
        },
        bottomBar = {
            AppBottomBar(
                onSettingsClick = {
                    viewModel.openSettings()
                },
                onHomeClick = {
                    viewModel.openLessons()
                },
                uiState = uiState
            )
        },
        content = { paddingValues ->
            AppContent(modifier = Modifier.padding(paddingValues), uiState = uiState)
        }
    )
}

@Composable
private fun ScreenTopBar() {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
    Row() {
        Spacer(
            modifier = Modifier
                .height(statusBarPadding.calculateTopPadding())
        )
    }
}

@Composable
private fun AppBottomBar(
    onSettingsClick: () -> Unit,
    onHomeClick: () -> Unit,
    uiState: State<LessonMenuUiState>
) {
    NavigationBar(containerColor = AppTransparencyColor) {
        NavigationBarItem(
            selected = uiState.value.lessonMenuScreenUiState == LessonMenuScreenUiState.Settings,
            onClick = {
                onSettingsClick()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = stringResource(R.string.lesson_settings_settings),
                    tint = AppContentColor
                )
            }, colors = NavigationBarItemDefaults.colors(
                indicatorColor = AppButtonBackgroundColor
            )
        )
        NavigationBarItem(
            selected = uiState.value.lessonMenuScreenUiState == LessonMenuScreenUiState.Lessons,
            onClick = {
                onHomeClick()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.lesson_settings_lessons),
                    tint = AppContentColor
                )
            }, colors = NavigationBarItemDefaults.colors(
                indicatorColor = AppButtonBackgroundColor
            )
        )
    }
}

@Composable
private fun AppContent(modifier: Modifier, uiState: State<LessonMenuUiState>) {
    Box(
        modifier = modifier
    ) {
        when (uiState.value.lessonMenuScreenUiState) {
            LessonMenuScreenUiState.Settings -> {
                SettingsContent()
            }

            LessonMenuScreenUiState.Lessons -> {
                LessonsContent()
            }
        }
    }
}

@Composable
private fun LessonsContent(viewModel: LessonMenuViewModel = hiltViewModel()) {
    val lessonList = viewModel.lessonList.collectAsStateWithLifecycle(emptyList())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Spacer(
                modifier = Modifier
                    .height(60.dp)
            )
            CentralTopBarText(text = stringResource(R.string.lessons_label))

            if (lessonList.value.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.create_your_first_lesson),
                        style = TextStyle(
                            fontFamily = nunitoVariableFont,
                            fontSize = 20.sp,
                            color = AppContentColor
                        )
                    )
                }
            } else {
                LazyColumn() {
                    items(lessonList.value) { lesson ->
                        LessonItem(
                            lessonName = lesson.name,
                            onDelete = {
                                viewModel.openDeleteLessonDialog(lesson)
                            },
                            chooseLesson = {
                                viewModel.updateCurrentLesson(lesson)
                            },
                            onStartLearning = {
                                viewModel.openLearningScreen(lesson)
                            },
                            isSelected = lesson == viewModel.uiState.value.currentLessonToLearn,
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(AppButtonBackgroundColor)
                    .clickable(onClick = {
                        viewModel.openCreatingLessonDialog()
                    }), contentAlignment = Alignment.Center
            ) {
                Text(
                    "+", style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = nunitoVariableFont,
                        color = AppContentColor, textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@Composable
private fun SettingsContent(viewModel: LessonMenuViewModel = hiltViewModel()) {
    val blueprintList = viewModel.blueprintList.collectAsStateWithLifecycle(emptyList())
    logHere()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Spacer(
                modifier = Modifier
                    .height(60.dp)
            )
            CentralTopBarText(text = stringResource(R.string.blueprints))

            if (blueprintList.value.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.create_your_first_blueprint),
                        style = TextStyle(
                            fontFamily = nunitoVariableFont,
                            fontSize = 20.sp,
                            color = AppContentColor
                        )
                    )
                }
            } else {
                LazyColumn() {
                    items(blueprintList.value) { blueprint ->
                        BlueprintItem(
                            blueprintName = blueprint.name,
                            onDelete = {
                                viewModel.openDeleteBlueprintDialog(blueprint)
                            }
                        )
                        Spacer(
                            modifier = Modifier
                                .height(8.dp)
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(AppButtonBackgroundColor)
                    .clickable(onClick = {
                        viewModel.createBlueprintButtonClick()
                    }), contentAlignment = Alignment.Center
            ) {
                Text(
                    "+", style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = nunitoVariableFont,
                        color = AppContentColor, textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@Composable
fun CentralTopBarText(
    text: String,
    barHeight: Int = 80,
    color: Color = AppContentColor,
    style: TextStyle = TextStyle(
        fontFamily = nunitoVariableFont,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        color = color
    )
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(barHeight.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = style
        )
    }
}