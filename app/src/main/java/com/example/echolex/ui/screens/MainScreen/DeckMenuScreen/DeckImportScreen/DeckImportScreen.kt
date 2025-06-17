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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels.DeckImportViewModel
import com.example.echolex.ui.customDesign.AppBorderButton
import com.example.echolex.ui.customDesign.AppButton
import com.example.echolex.ui.customDesign.AppMultiLineOutlinedTextField
import com.example.echolex.ui.customDesign.AppOutlinedTextField
import com.example.echolex.ui.customDesign.StandardStart
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonContentColor
import com.example.echolex.ui.theme.AppContentAltColor
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.fugazOneFontFamily

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
                        .height(65.dp)
                )
                Text(
                    text = "IMPORT DECK",
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontFamily = fugazOneFontFamily,
                        color = AppButtonContentColor
                    ),
                    modifier = Modifier.padding(bottom = 30.dp)
                )

                Spacer(
                    modifier = Modifier
                        .height(130.dp)
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
                        Text(
                            text = "Deck name",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = fugazOneFontFamily,
                                color = AppContentAltColor
                            ),
                            modifier = Modifier.padding(start = 20.dp)
                        )

                        // Deck Name Input
                        AppOutlinedTextField(
                            value = viewModel.nameTextField.value,
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
                        Text(
                            text = "Import input",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = fugazOneFontFamily,
                                color = AppContentAltColor
                            ),
                            modifier = Modifier.padding(start = 20.dp)
                        )

                        // Import Multi-line Input
                        AppMultiLineOutlinedTextField(
                            value = viewModel.importTextField.value,
                            onValueChange = viewModel::onImportTextChanged, // Тобі треба окремий метод для цього поля
                            placeholderText = """
                        Format:
                        word, translate;
                        word, translate;
                    """.trimIndent(),
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
                                text = "PASTE",
                                onClick = {
                                    val clipData = clipboardManager.primaryClip
                                    val item = clipData?.getItemAt(0)
                                    val pasteText = item?.text?.toString() ?: ""
                                    viewModel.pasteImportText(pasteText)
                                },
                                modifier = Modifier.width(150.dp)
                            )
                            AppButton(
                                text = "CLEAR",
                                onClick = {
                                    viewModel.clearImportText()
                                },
                                modifier = Modifier.width(150.dp)
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
                            Spacer(modifier = Modifier.width(16.dp))

                            Checkbox(
                                checked = viewModel.isMarkingLikePreLearned.value,
                                onCheckedChange = { viewModel.toggleMarkAsPreLearned() }
                            )
                            Spacer(modifier = Modifier.width(5.dp))

                            Text(
                                "Mark as pre-learned", style = TextStyle(
                                    fontSize = 15.sp,
                                    fontFamily = fugazOneFontFamily,
                                    color = AppContentColor
                                )
                            )
                        }
                    }
                    AppBorderButton(
                        text = "CREATE DECK",
                        modifier = Modifier,
                        onClick = {
                            viewModel.startCreatingDeck()
                        })
                }

            }


        }
    }
}
