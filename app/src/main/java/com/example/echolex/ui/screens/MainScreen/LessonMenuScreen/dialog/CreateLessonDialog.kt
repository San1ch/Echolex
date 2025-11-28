package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.dialog


import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.echolex.R
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuViewModel
import com.example.echolex.ui.customDesign.AppOutlinedTextField
import com.example.echolex.ui.customDesign.AppStandardDialogBackground
import com.example.echolex.ui.customDesign.DualButtonRow
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.item.BlueprintItemInLessonCreating
import com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.item.DeckItemInLessonCreating
import com.example.echolex.ui.theme.AppButtonBorderColor
import com.example.echolex.ui.theme.AppButtonContentColor
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

fun logHere(tag: String = "MyLog", msg: String = "", callerDepth: Int = 2) {
    val stack = Throwable().stackTrace
    val s = stack.getOrNull(callerDepth) ?: stack.last()
    Log.d(tag, "${s.fileName}:${s.lineNumber}.${s.methodName} $msg")
}
@Composable
fun CreateLessonDialog(viewModel: LessonMenuViewModel) {

    val blueprintList = viewModel.blueprintList.collectAsStateWithLifecycle(emptyList())
    logHere()
    val deckList = viewModel.deckList.collectAsState()

    var selectedIndex by remember { mutableIntStateOf(0) }
    val checkedStates = remember { mutableStateOf(List(deckList.value.size) { false }) }

    AppStandardDialogBackground {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.create_lesson), style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppContentColor,
                ),
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Name", style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = nunitoVariableFont,
                        color = AppContentColor
                    )
                )
                Spacer(modifier = Modifier
                            .width(8.dp))
                AppOutlinedTextField(
                    value = viewModel.uiState.value.lessonNameTextField,
                    onValueChange = { viewModel.onLessonNameChange(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(8.dp))
            // BLUEPRINT
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, AppButtonBorderColor, shape = RoundedCornerShape(5.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.blueprints),
                        color = AppButtonContentColor,
                        fontFamily = nunitoVariableFont,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(start = 12.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    ) {
                        items(blueprintList.value) { blueprint ->
                            val index = blueprintList.value.indexOf(blueprint)
                            BlueprintItemInLessonCreating(
                                blueprintName = blueprint.name,
                                isSelected = selectedIndex == index,
                                onClick = { selectedIndex = index }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // DECK
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, AppButtonBorderColor, shape = RoundedCornerShape(5.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = stringResource(R.string.decks),
                            color = AppButtonContentColor,
                            fontFamily = nunitoVariableFont,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(start = 12.dp)
                        )
                        IconButton(
                            onClick = {
                                checkedStates.value = List(deckList.value.size) { true }
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.List,
                                    contentDescription = stringResource(
                                        R.string.choose_all
                                    ),
                                    tint = AppButtonContentColor
                                )
                            }, modifier = Modifier
                                .height(30.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    ) {
                        items(deckList.value.size) { index ->
                            DeckItemInLessonCreating(
                                deckName = deckList.value[index].name,
                                countOfCards = deckList.value[index].cards.size.toString(),
                                isChecked = checkedStates.value[index],
                                onCheckedChange = {
                                    val updatedList = checkedStates.value.toMutableList()
                                    updatedList[index] = it
                                    checkedStates.value = updatedList
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            DualButtonRow(
                leftButtonText = stringResource(R.string.cancel),
                rightButtonText = stringResource(R.string.create),
                onLeftClick = { viewModel.dialogCenter.closeDialog() },
                onRightClick = {
                    val selectedBlueprint = blueprintList.value.getOrNull(selectedIndex)
                    if (selectedBlueprint != null) {
                        viewModel.createLesson(
                            lessonBlueprint = selectedBlueprint,
                            decks = deckList.value.filterIndexed { index, _ -> checkedStates.value[index] })
                    }
                },
            )
        }
    }
}