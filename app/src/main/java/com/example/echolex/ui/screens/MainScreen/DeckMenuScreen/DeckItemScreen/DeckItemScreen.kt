package com.example.echolex.ui.screens.MainScreen.DeckMenuScreen.DeckItemScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.echolex.core.data.model.dataclass.Card
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels.DeckItemViewModel
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels.DeckItemViewModel.DeckItemDialogMode
import com.example.echolex.ui.customDesign.AppBorderButton
import com.example.echolex.ui.customDesign.AppMultiLineOutlinedTextField
import com.example.echolex.ui.customDesign.AppOutlinedTextField
import com.example.echolex.ui.customDesign.StandardStart
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonBorderColor
import com.example.echolex.ui.theme.AppCardItemBackgroundColor
import com.example.echolex.ui.theme.AppCardItemBorderColor
import com.example.echolex.ui.theme.AppCardItemContentColor
import com.example.echolex.ui.theme.AppCardItemLearnedStatusColor
import com.example.echolex.ui.theme.AppCardItemNotLearnedStatusColor
import com.example.echolex.ui.theme.AppCardItemPreLearnedStatusColor
import com.example.echolex.ui.theme.AppContentAltColor
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.fugazOneFontFamily
import androidx.compose.runtime.getValue


@Composable
fun DeckItemScreen(viewModel: DeckItemViewModel = hiltViewModel()) {
    DeckItemScreenContent(viewModel)
}

@Composable
fun DeckItemScreenContent(viewModel: DeckItemViewModel) {
    val localDeck by viewModel.localDeck
    val centralDeck by viewModel.screenDeck.collectAsState()
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
                contentDescription = "Deck deleter",
                tint = AppContentAltColor,
                modifier = Modifier
                    .clickable() {
                        viewModel.openDeleteDeckDialog()
                    }
            )
            Text(
                text = centralDeck.name,
                fontSize = 15.sp,
                fontFamily = fugazOneFontFamily,
                color = AppContentColor,
                modifier = Modifier.clickable() {
                    viewModel.openChangeNameDialog()
                }
            )
            Text(
                localDeck.cards.size.toString(), style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = fugazOneFontFamily,
                    color = AppContentAltColor
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
                            "Deck is empty", style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = fugazOneFontFamily,
                                color = AppCardItemContentColor
                            )
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(520.dp)
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
                        AppBorderButton(
                            text = "IMPORT",
                            onClick = {
                                viewModel.openImportDialog()
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(
                            modifier = Modifier
                                .width(5.dp)
                        )
                        AppBorderButton(
                            text = "EXPORT",
                            onClick = { },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                    AppBorderButton(
                        text = "Back",
                        onClick = {
                            viewModel.navigateDeckMenu()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }
    DeckItemDialogs(viewModel)
}

@Composable
private fun DeckItemDialogs(viewModel: DeckItemViewModel) {
    DeckItemDialogRemoveDeck(viewModel)
    DeckItemDialogChangeName(viewModel)
    DeckItemDialogImport(viewModel)
    DeckItemDialogRemoveCard(viewModel)
}

@Composable
private fun DeckItemDialogRemoveDeck(viewModel: DeckItemViewModel) {
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
                        "Remove deck?", style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = fugazOneFontFamily,
                            color = AppCardItemContentColor
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
                        AppBorderButton("YES", {
                            viewModel.deleteDeck()
                        }, modifier = Modifier.width(150.dp))
                        Spacer(modifier = Modifier.width(15.dp))
                        AppBorderButton("NO", {
                            viewModel.cancelDeleting()
                        }, modifier = Modifier.width(150.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DeckItemDialogImport(viewModel: DeckItemViewModel) {
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
                    .height(220.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Import to deck", style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = fugazOneFontFamily,
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
                    AppBorderButton(
                        "Add cards", {
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
private fun DeckItemDialogChangeName(viewModel: DeckItemViewModel) {

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
                        "New name", style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = fugazOneFontFamily,
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
                    AppBorderButton("CHANGE", {
                        viewModel.changeDeckName()
                    }, modifier = Modifier)
                }
            }
        }
    }
}

@Composable
private fun DeckItemDialogRemoveCard(viewModel: DeckItemViewModel) {
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
                        "Delete card?", style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = fugazOneFontFamily,
                            color = AppCardItemContentColor
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
                        AppBorderButton("YES", {
                            viewModel.deleteCard()
                        }, modifier = Modifier.width(150.dp))
                        Spacer(modifier = Modifier.width(15.dp))
                        AppBorderButton("NO", {
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
        if (card.isPreLearnedCard) {
            if (card.countOfRepeating >= COUNT_OF_REPETITION_TO_LEARN) {
                AppCardItemLearnedStatusColor
            } else {
                AppCardItemPreLearnedStatusColor
            }
        } else {
            AppCardItemNotLearnedStatusColor
        }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = AppCardItemBackgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 2.dp,
                color = AppCardItemBorderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
            .clickable {
                onFlip()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                card.firstWord, style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = fugazOneFontFamily,
                    color = AppCardItemContentColor
                )
            )
            Row(
                modifier = Modifier
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Learned Status",
                    tint = learnedStatusColor
                )
                Text(
                    card.countOfRepeating.toString(), style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = fugazOneFontFamily,
                        color = AppCardItemBackgroundColor
                    )
                )
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Delete Card",
                    tint = AppContentColor,
                    modifier = Modifier.clickable() {
                        onDelete()
                    }
                )
            }
        }
    }
}
