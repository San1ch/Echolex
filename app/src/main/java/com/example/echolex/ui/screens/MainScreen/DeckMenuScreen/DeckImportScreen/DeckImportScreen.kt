package com.example.echolex.ui.screens.MainScreen.DeckMenuScreen.DeckImportScreen

import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.echolex.R
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels.DeckImportViewModel
import com.example.echolex.ui.customDesign.AppButton
import com.example.echolex.ui.customDesign.AppLabel
import com.example.echolex.ui.customDesign.AppMultiLineOutlinedTextField
import com.example.echolex.ui.customDesign.AppOutlinedTextField
import com.example.echolex.ui.customDesign.LabeledCheckbox
import com.example.echolex.ui.customDesign.StandardStart
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun DeckImportScreen (viewModel: DeckImportViewModel = hiltViewModel()){
    DeckImportScreenContent(viewModel)
}

@Composable
fun DeckImportScreenContent (viewModel: DeckImportViewModel){
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    StandardStart() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp, 40.dp, 25.dp, 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(
                    modifier = Modifier
                        .height(80.dp)
                )
                Text(
                    text = stringResource(R.string.import_deck),
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontFamily = nunitoVariableFont,
                        fontWeight = FontWeight.Black,
                        color = AppButtonContentColor
                    ),
                    modifier = Modifier.padding(bottom = 30.dp)
                )

                Spacer(
                    modifier = Modifier
                        .height(100.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        // Deck Name Label

                        AppLabel(
                            text = stringResource(R.string.deck_name),
                        )

                        // Deck Name Input
                        AppOutlinedTextField(
                            value = viewModel.uiState.value.nameText,
                            onValueChange = viewModel::onNameTextChanged,
                            backgroundColor = AppButtonBackgroundColor,
                            borderColor = AppButtonBackgroundColor,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                        )
                        // Import Input Label
                        AppLabel(
                            text = stringResource(R.string.import_input),
                        )

                        // Import Multi-line Input
                        AppMultiLineOutlinedTextField(
                            value = viewModel.uiState.value.importText,
                            onValueChange = viewModel::onImportTextChanged,
                            placeholderText = stringResource(R.string.import_format).trimIndent(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            borderColor = AppButtonBackgroundColor
                        )

                        // Buttons and Checkbox Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            AppButton(
                                text = stringResource(R.string.paste),
                                onClick = {
                                    val clipData = clipboardManager.primaryClip
                                    val item = clipData?.getItemAt(0)
                                    val pasteText = item?.text?.toString() ?: ""
                                    viewModel.pasteImportText(pasteText)
                                },
                                modifier = Modifier.width(150.dp),
                                cornerRadius = 7
                            )
                            AppButton(
                                text = stringResource(R.string.clear),
                                onClick = {
                                    viewModel.clearImportText()
                                },
                                modifier = Modifier.width(150.dp),
                                cornerRadius = 7
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                        )
                        Row(
                            modifier = Modifier.wrapContentSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LabeledCheckbox(
                                checked = viewModel.uiState.value.markAsPreLearned,
                                onCheckedChange = { viewModel.toggleMarkAsPreLearned() },
                                text = stringResource(R.string.mark_pre_learned),

                            )
                        }
                    }
                    AppButton(
                        text = stringResource(R.string.create_deck),
                        modifier = Modifier,
                        onClick = {
                            viewModel.startCreatingDeck()
                        })
                }

            }


        }
    }
}

