package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonLearningUiState
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonLearningViewModel
import com.example.echolex.ui.customDesign.StandardStart
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun LearningScreen(
    viewModel: LessonLearningViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LearningContent(
        uiState = uiState,
        onCardCorrect = { viewModel.onCardCorrect() },
        onCardIncorrect = { viewModel.onCardIncorrect() },
        onFlipCard = { viewModel.flipCard() },
        onDismissError = { viewModel.dismissError() }
    )
}

@Composable
fun LearningContent(
    uiState: LessonLearningUiState,
    onCardCorrect: () -> Unit,
    onCardIncorrect: () -> Unit,
    onFlipCard: () -> Unit,
    onDismissError: () -> Unit
) {
    StandardStart {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is LessonLearningUiState.Loading -> {
                    LoadingState()
                }
                
                is LessonLearningUiState.ShowCard -> {
                    CardDisplay(
                        card = uiState.card,
                        stageInfo = uiState.stageInfo,
                        progress = uiState.progress,
                        onFlipCard = onFlipCard,
                        onCardCorrect = onCardCorrect,
                        onCardIncorrect = onCardIncorrect
                    )
                }
                
                is LessonLearningUiState.Error -> {
                    ErrorState(
                        notification = uiState.notification,
                        onDismiss = onDismissError
                    )
                }
                
                is LessonLearningUiState.Completed -> {
                    CompletedState()
                }
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CardDisplay(
    card: Card,
    stageInfo: String,
    progress: Float,
    onFlipCard: () -> Unit,
    onCardCorrect: () -> Unit,
    onCardIncorrect: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Прогрес бар
        LinearProgressIndicator(
            progress = progress / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // Інформація про етап
        Text(
            text = stageInfo,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = nunitoVariableFont,
                color = AppContentColor
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Карта
        androidx.compose.material3.Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clickable { onFlipCard() }
                .padding(bottom = 32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = card.firstWord,
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontFamily = nunitoVariableFont,
                        fontWeight = FontWeight.Bold,
                        color = AppContentColor,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        // Підказка
        Text(
            text = "Натисніть на карту, щоб перевернути",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = nunitoVariableFont,
                color = AppContentColor.copy(alpha = 0.7f)
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Кнопки відповідей
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onCardIncorrect,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(
                    text = "Неправильно",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = nunitoVariableFont,
                        color = Color.White
                    )
                )
            }
            
            Button(
                onClick = onCardCorrect,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(
                    text = "Правильно",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = nunitoVariableFont,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun ErrorState(
    notification: com.example.echolex.core.domain.data.model.notification.AppNotification,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Red.copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = notification.title,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = nunitoVariableFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = notification.message,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = nunitoVariableFont,
                        color = AppContentColor
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text(
                        text = "Спробувати знову",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = nunitoVariableFont,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CompletedState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Green.copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉 Урок завершено!",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = nunitoVariableFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "Вітаємо! Ви успішно завершили урок.",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = nunitoVariableFont,
                        color = AppContentColor
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

