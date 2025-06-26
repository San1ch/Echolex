package com.example.echolex.ui.screens.MainScreen.LessonSettingsScreen.dialog


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuViewModel
import com.example.echolex.ui.screens.MainScreen.LessonSettingsScreen.item.BlueprintItemCreating
import com.example.echolex.ui.screens.MainScreen.LessonSettingsScreen.item.DeckItemInLessonCreating


@Composable
fun CreateLessonDialog(viewModel: LessonMenuViewModel) {
    val blueprintList = viewModel.blueprintList.collectAsState()
    val deckList = viewModel.deckList.collectAsState()

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(20.dp)
    ) {
        Row(modifier = Modifier.wrapContentSize()) {
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .width(200.dp)
            ) {
                LazyColumn {
                    items(blueprintList.value.size) { index ->
                        BlueprintItemCreating {
                        }
                    }
                }
                Column(modifier = Modifier.wrapContentSize()) {
                    LazyColumn {
                        items(deckList.value.size) { index ->
                            DeckItemInLessonCreating(
                                deckName = deckList.value[index].name,
                                countOfCards = deckList.value[index].cards.size.toString()
                            ) {

                            }
                        }
                    }
                }
            }
        }
    }
}