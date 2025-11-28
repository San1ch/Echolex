package com.example.echolex.ui.screens.MainScreen.DeckMenuScreen.DeckItemScreen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.echolex.core.constants.COUNT_OF_REPETITION_TO_LEARN
import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels.DeckItemViewModel
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels.DeckItemViewModel.DeckItemDialogMode
import com.example.echolex.ui.customDesign.AppBorderButton
import com.example.echolex.ui.customDesign.AppMultiLineOutlinedTextField
import com.example.echolex.ui.customDesign.AppOutlinedTextField
import com.example.echolex.ui.customDesign.StandardStart
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonBorderColor
import com.example.echolex.ui.theme.AppCardItemLearnedStatusColor
import com.example.echolex.ui.theme.AppCardItemNotLearnedStatusColor
import com.example.echolex.ui.theme.AppCardItemPreLearnedStatusColor
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.echolex.R
import com.example.echolex.ui.customDesign.AppButton
import com.example.echolex.ui.customDesign.ItemRowBackground
import com.example.echolex.ui.customDesign.LabeledCheckbox
import com.example.echolex.ui.theme.AppButtonContentColor


@Composable
fun DeckItemScreen(viewModel: DeckItemViewModel = hiltViewModel()) {
    DeckItemScreenContent(viewModel)
}

@Composable
fun DeckItemScreenContent(viewModel: DeckItemViewModel) {
    val localDeck by viewModel.localDeck
    val centralDeck by viewModel.screenDeck.collectAsState()

    val context = LocalContext.current
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


    StandardStart {
        Spacer(
            modifier = Modifier
                .height(100.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.remove_deck),
                tint = AppContentColor,
                modifier = Modifier
                    .clickable() {
                        viewModel.openDeleteDeckDialog()
                    }
            )
            Text(
                text = centralDeck.name,
                fontSize = 15.sp,
                fontFamily = nunitoVariableFont,
                color = AppContentColor,
                modifier = Modifier.clickable() {
                    viewModel.openChangeNameDialog()
                }
            )
            Text(
                localDeck.cards.size.toString(), style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppContentColor
                )
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Card list
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Box() {
                if (localDeck.cards.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            stringResource(R.string.deck_is_empty), style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = nunitoVariableFont,
                                color = AppContentColor
                            )
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        LazyColumn(modifier = Modifier) {
                            items(localDeck.cards.size) { card ->
                                CardItemView(
                                    card = localDeck.cards[card],
                                    onFlip = { viewModel.flipCard(localDeck.cards[card]) },
                                    onDelete = {
                                        viewModel.openDeletingDialog(centralDeck.cards[card])
                                    })
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
            // Buttons
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppButton(
                            text = stringResource(R.string.Import),
                            onClick = {
                                viewModel.openImportDialog()
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(
                            modifier = Modifier
                                .width(5.dp)
                        )
                        AppButton(
                            text = stringResource(R.string.export),
                            onClick = {
                                viewModel.exportDeck { exportedText ->
                                    val clip = ClipData.newPlainText(
                                        context.getString(R.string.exported_deck),
                                        exportedText
                                    )
                                    clipboard.setPrimaryClip(clip)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )

                    }
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                    AppButton(
                        text = stringResource(R.string.back),
                        onClick = {
                            viewModel.backScreen()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }
    Dialogs(viewModel)
}

@Composable
private fun Dialogs(viewModel: DeckItemViewModel) {
    DialogRemoveDeck(viewModel)
    DialogChangeName(viewModel)
    DialogImport(viewModel)
    DialogRemoveCard(viewModel)
}

@Composable
private fun DialogRemoveDeck(viewModel: DeckItemViewModel) {
    AnimatedVisibility(
        visible = viewModel.dialogMode.value is DeckItemDialogMode.RemoveDeck,
        enter = fadeIn(animationSpec = tween<Float>(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable(
                    onClick = { viewModel.closeDialogMode() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.remove_deck), style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = nunitoVariableFont,
                            color = AppButtonContentColor
                        )
                    )
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppBorderButton(stringResource(R.string.yes), {
                            viewModel.deleteDeck()
                        }, modifier = Modifier.width(150.dp))
                        Spacer(modifier = Modifier.width(15.dp))
                        AppBorderButton(stringResource(R.string.no), {
                            viewModel.cancelDeleting()
                        }, modifier = Modifier.width(150.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogImport(viewModel: DeckItemViewModel) {
    AnimatedVisibility(
        visible = viewModel.dialogMode.value is DeckItemDialogMode.ImportCards,
        enter = fadeIn(animationSpec = tween<Float>(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable(
                    onClick = { viewModel.closeDialogMode() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(AppButtonBackgroundColor)
                    .animateContentSize()
                    .clickable(
                        onClick = { },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .padding(24.dp)
                    .width(300.dp)
                    .height(270.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.import_to_deck), style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = nunitoVariableFont,
                            color = AppContentColor
                        )
                    )
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                    AppMultiLineOutlinedTextField(
                        value = viewModel.importTextField.value,
                        onValueChange = { viewModel.onImportChange(it) },
                        placeholderText = """   
                        Format:
                        word, translate;
                        word, translate;
                    """.trimIndent(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        borderColor = AppButtonBorderColor
                    )
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                    LabeledCheckbox(
                        checked = viewModel.withFlipCards.value,
                        onCheckedChange = { viewModel.toggleWithFlipCards() },
                        text = stringResource(R.string.with_flip_card_list),

                    )
                    AppBorderButton(
                        stringResource(R.string.add_cards), {
                            viewModel.addImportCards()
                        }, modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogChangeName(viewModel: DeckItemViewModel) {

    AnimatedVisibility(
        visible = viewModel.dialogMode.value is DeckItemDialogMode.ChangeDeckName,
        enter = fadeIn(animationSpec = tween<Float>(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable(
                    onClick = { viewModel.closeDialogMode() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(AppButtonBackgroundColor)
                    .animateContentSize()
                    .clickable(
                        onClick = { },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .padding(24.dp)
                    .width(300.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.new_name), style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = nunitoVariableFont,
                            color = AppContentColor
                        )
                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    AppOutlinedTextField(
                        value = viewModel.changedNameTextField.value,
                        onValueChange = { viewModel.onChangeNameChange(it) },
                        modifier = Modifier

                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    AppBorderButton(stringResource(R.string.change), {
                        viewModel.changeDeckName()
                    }, modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun DialogRemoveCard(viewModel: DeckItemViewModel) {
    AnimatedVisibility(
        visible = viewModel.dialogMode.value is DeckItemDialogMode.RemoveCard,
        enter = fadeIn(animationSpec = tween<Float>(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable(
                    onClick = { viewModel.closeDialogMode() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(AppButtonBackgroundColor)
                    .animateContentSize()
                    .clickable(
                        onClick = { },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .padding(24.dp)
                    .width(300.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.remove_card), style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = nunitoVariableFont,
                            color = AppButtonContentColor
                        )
                    )
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppBorderButton(stringResource(R.string.yes), {
                            viewModel.deleteCard()
                        }, modifier = Modifier.width(150.dp))
                        Spacer(modifier = Modifier.width(15.dp))
                        AppBorderButton(stringResource(R.string.no), {
                            viewModel.cancelDeleting()
                        }, modifier = Modifier.width(150.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CardItemView(card: Card, onFlip: () -> Unit, onDelete: () -> Unit) {

    val learnedStatusColor =
        if (card.isPreLearned) {
            if (card.repeatingCount >= COUNT_OF_REPETITION_TO_LEARN) {
                AppCardItemLearnedStatusColor
            } else {
                AppCardItemPreLearnedStatusColor
            }
        } else {
            AppCardItemNotLearnedStatusColor
        }
    ItemRowBackground(
        onBackgroundClick = { onFlip() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                card.firstWord, style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppButtonContentColor
                )
            )
            Row(
                modifier = Modifier
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.learn_status),
                    tint = learnedStatusColor
                )
                Spacer(
                    modifier = Modifier
                        .width(8.dp)
                )
                Text(
                    card.repeatingCount.toString(), style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = nunitoVariableFont,
                        color = AppContentColor
                    )
                )
                Spacer(
                    modifier = Modifier
                        .width(8.dp)
                )
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.remove_card),
                    tint = AppContentColor,
                    modifier = Modifier.clickable() {
                        onDelete()
                    }.size(25.dp)
                )
            }
        }
    }
}
