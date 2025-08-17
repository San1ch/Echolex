package com.example.echolex.ui.screens.MainScreen.DeckMenuScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.echolex.R
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels.DecksMenuViewModel
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels.DialogState
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels.DialogType
import com.example.echolex.ui.customDesign.AppBorderButton
import com.example.echolex.ui.customDesign.AppButton
import com.example.echolex.ui.customDesign.AppOutlinedTextField
import com.example.echolex.ui.customDesign.StandardStart
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun DecksScreen(viewModel: DecksMenuViewModel = hiltViewModel()) {
    DecksScreenContent(viewModel)
}

@Composable
fun DecksScreenContent(viewModel: DecksMenuViewModel) {
    val decks by viewModel.decks.collectAsState()
    val dialogState by viewModel.dialogState.collectAsState()

    StandardStart() {
        Box() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp, 25.dp, 15.dp, 65.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                //Header
                Box() {

                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(150.dp)
                        )
                        Row() {
                            Text(
                                stringResource(R.string.deck_count), style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = nunitoVariableFont,
                                    color = AppButtonContentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                decks.size.toString(), style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = nunitoVariableFont,
                                    color = AppButtonContentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )

                    if (viewModel.countOfDecks > 0) {
                        LazyColumn() {
                            items(viewModel.countOfDecks) { item ->
                                val data = viewModel.getAllItemDeckData()
                                ItemDeckBoard(
                                    nameOfDeck = data[item].name,
                                    countOfCards = data[item].learningStatusCounts.countCard,
                                    countOfNotLearnedCards = data[item].learningStatusCounts.countNotLearnedCard,
                                    countOfPreLearnedCards = data[item].learningStatusCounts.countPreLearnedCard,
                                    countOfLearnedCards = data[item].learningStatusCounts.countLearnedCard,
                                    viewModel,
                                )
                            }
                        }
                    }
                }
                //Bottom Part
                AppButton(text = stringResource(R.string.create_deck), onClick = {
                    viewModel.openChooseModeDialog()
                }, modifier = Modifier)
            }
        }
    }
    Dialogs(dialogState, viewModel)
}

@Composable
private fun Dialogs(dialogState: DialogState, viewModel: DecksMenuViewModel) {
    when (dialogState.type) {
        DialogType.ChooseMode -> {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CreateDeckChooseDialog(
                    onDismiss = viewModel::closeDialog,
                    onEmptyCreator = viewModel::openEmptyCreatorDialog,
                    onOpenImportScreen = viewModel::navigateImportScreen
                )
            }
        }

        DialogType.EmptyCreator -> {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CreateEmptyDeckDialog(
                    deckName = dialogState.deckName,
                    onDismiss = viewModel::closeDialog,
                    onDeckNameChanged = viewModel::onDeckNameChanged,
                    onCreate = viewModel::createDeck
                )
            }
        }

        DialogType.None -> {}
    }

}


@Composable
fun ItemDeckBoard(
    nameOfDeck: String,
    countOfCards: String,
    countOfNotLearnedCards: String,
    countOfPreLearnedCards: String,
    countOfLearnedCards: String,
    viewModel: DecksMenuViewModel
) {
    val fontSize = 13.sp

    Box(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 5.dp,
                    topEnd = 35.dp,
                    bottomStart = 35.dp,
                    bottomEnd = 5.dp
                )
            )
            .background(AppButtonBackgroundColor)
            .height(100.dp)
            .fillMaxWidth()
            .clickable { viewModel.navigateDeckInfoScreen(nameOfDeck) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, top = 10.dp, end = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    nameOfDeck,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = nunitoVariableFont,
                        color = AppButtonContentColor,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Column(modifier = Modifier.weight(10f)) {
                    InfoRow(stringResource(R.string.count_of_card), countOfCards, fontSize)
                    InfoRow(stringResource(R.string.learned_cards), countOfLearnedCards, fontSize)
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(modifier = Modifier.weight(12f)) {
                    InfoRow(stringResource(R.string.pre_learned_cards), countOfPreLearnedCards, fontSize)
                    InfoRow(stringResource(R.string.not_learned_cards), countOfNotLearnedCards, fontSize)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, fontSize: TextUnit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = TextStyle(
                fontSize = fontSize,
                fontFamily = nunitoVariableFont,
                color = AppButtonContentColor,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            value,
            style = TextStyle(
                fontSize = fontSize,
                fontFamily = nunitoVariableFont,
                color = AppButtonContentColor,
                fontWeight = FontWeight.Medium
            )
        )
    }
}


@Composable
fun CreateDeckChooseDialog(
    onDismiss: () -> Unit,
    onEmptyCreator: () -> Unit,
    onOpenImportScreen: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(
                onClick = { onDismiss() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .width(300.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(AppButtonBackgroundColor)
                .animateContentSize()
                .clickable(
                    onClick = { },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AppBorderButton(stringResource(R.string.import_list), {
                    onOpenImportScreen()
                }, modifier = Modifier)
                Spacer(modifier = Modifier.height(15.dp))
                AppBorderButton(stringResource(R.string.create_empty), {
                    onEmptyCreator()
                }, modifier = Modifier)
            }
        }
    }
}


@Composable
fun CreateEmptyDeckDialog(
    deckName: String,
    isLoading: Boolean = false,
    error: String? = null,
    onDismiss: () -> Unit,
    onDeckNameChanged: (String) -> Unit,
    onCreate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(
                onClick = { onDismiss() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .width(300.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(AppButtonBackgroundColor)
                .animateContentSize()
                .clickable(
                    onClick = { },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.choose_name), style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = nunitoVariableFont,
                        fontWeight = FontWeight.Medium,
                        color = AppButtonContentColor
                    )
                )

                error?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = nunitoVariableFont
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }


                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                AppOutlinedTextField(
                    value = deckName,
                    onValueChange = { onDeckNameChanged(it) },
                    singleLine = true,
                    placeholderText = stringResource(R.string.deck_name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 0.dp)
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                } else {
                    AppBorderButton(stringResource(R.string.create_deck_dialog), {
                        onCreate()
                    }, modifier = Modifier.padding(vertical = 10.dp))
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DeckMenuScreenPreview() {
    DecksScreen()
}

