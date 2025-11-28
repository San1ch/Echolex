package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonLearningEvent
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonLearningUiState
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonLearningViewModel

@Composable
fun LearningScreen(viewModel: LessonLearningViewModel = hiltViewModel()) {
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.event.collect { e ->
            when (e) {
                is LessonLearningEvent.ShowToast -> {
                    Toast
                        .makeText(context, e.notification.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    LearningScreenContent(
        uiState = viewModel.uiState.collectAsState().value,
        viewModel = viewModel
    )
}

@Composable
fun LearningScreenContent(
    uiState: LessonLearningUiState,
    viewModel: LessonLearningViewModel
) {
    val leftBg = Color(0xFF2F0000)
    val rightBg = Color(0xFF002C00)


    val topBarColor = if (uiState.wasIncorrect) {
        Color(0xFFFFB4B4)
    } else {
        Color(0xFFB8F3C3)
    }

    val noRipple = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(listOf(leftBg, rightBg))
            )
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            val (topBar, leftZone, rightZone, centerWord, bottomBar) = createRefs()
            val mid = createGuidelineFromStart(0.5f)

            
            Row(
                modifier = Modifier
                    .constrainAs(topBar) {
                        top.linkTo(parent.top, margin = 10.dp)      
                        start.linkTo(parent.start, margin = 12.dp)  
                        end.linkTo(parent.end, margin = 12.dp)
                        width = Dimension.fillToConstraints
                    }
                    .clip(RoundedCornerShape(22.dp))
                    .background(topBarColor) 
                    .heightIn(min = 54.dp)   
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Cards: ${uiState.remainingCards + 1}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "Stage: ${uiState.currentIndexStage + 1}/${uiState.stageCount}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "Cycles: ${uiState.remainingCycles}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            
            Box(
                modifier = Modifier
                    .constrainAs(bottomBar) {
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                        start.linkTo(parent.start, margin = 12.dp)
                        end.linkTo(parent.end, margin = 12.dp)
                        width = Dimension.fillToConstraints
                    }
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.White)
                    .heightIn(min = 56.dp)
                    .clickable(interactionSource = noRipple, indication = null) {
                        viewModel.onExit()
                    }
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Exit",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            
            Box(
                modifier = Modifier
                    .constrainAs(leftZone) {
                        top.linkTo(topBar.bottom)
                        bottom.linkTo(bottomBar.top)
                        start.linkTo(parent.start)
                        end.linkTo(mid)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { viewModel.onDoNotKnow() }
            )

            
            Box(
                modifier = Modifier
                    .constrainAs(rightZone) {
                        top.linkTo(topBar.bottom)
                        bottom.linkTo(bottomBar.top)
                        start.linkTo(mid)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { viewModel.onKnow() }
            )

            
            Box(
                modifier = Modifier
                    .constrainAs(centerWord) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(topBar.bottom)
                        bottom.linkTo(bottomBar.top)
                    }
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { viewModel.flipCard() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (uiState.isFlipped) uiState.card.secondWord else uiState.card.firstWord,
                    style = TextStyle(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}