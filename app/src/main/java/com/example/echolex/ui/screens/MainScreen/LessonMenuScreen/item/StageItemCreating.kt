package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.core.domain.data.model.lesson.LessonStage
import com.example.echolex.core.domain.data.model.lesson.StageType
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonBorderColor
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun StageItemCreating(
    stage: LessonStage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AppButtonBackgroundColor)
            .border(1.dp, AppButtonBorderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = when (stage.type) {
                    StageType.LEARNING -> "L"
                    StageType.REPEATING -> "R"
                },
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppContentColor
                )
            )
            
            Text(
                text = "${stage.cards}",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppContentColor
                )
            )
        }
    }
}
