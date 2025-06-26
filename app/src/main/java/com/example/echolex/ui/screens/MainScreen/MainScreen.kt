package com.example.echolex.ui.screens.MainScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.echolex.R
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.MainMenuUiState
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.MainMenuViewModel
import com.example.echolex.ui.customDesign.AppButton
import com.example.echolex.ui.customDesign.StandardStart

@Composable
fun MainScreen(viewModel: MainMenuViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState()

    when (uiState.value) {
        is MainMenuUiState.Success -> {
            MainMenuScreenContent(viewModel)
        }
        is MainMenuUiState.Error -> {

        }
        is MainMenuUiState.Loading -> {

        }
    }

}

@Composable
fun MainMenuScreenContent(viewModel: MainMenuViewModel) {
    StandardStart {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween){
                Column {

                }
                Column() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppButton(
                            stringResource(R.string.start), modifier = Modifier
                                .weight(1f)
                                .padding(5.dp, 0.dp), onClick = {
                            viewModel.openLessonMenu()
                        })
                        AppButton(
                            stringResource(R.string.decks), modifier = Modifier
                                .weight(1f)
                                .padding(5.dp, 0.dp), onClick = {
                            viewModel.openDecksMenu()
                        })
                    }
                    Spacer(modifier = Modifier
                        .height(20.dp))
                }
            }
        }
    }
}