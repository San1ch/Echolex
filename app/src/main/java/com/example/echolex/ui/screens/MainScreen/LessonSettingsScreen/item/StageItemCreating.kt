package com.example.echolex.ui.screens.MainScreen.LessonSettingsScreen.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.R
import com.example.echolex.core.domain.data.model.lesson.LearningStage
import com.example.echolex.core.domain.data.model.lesson.LessonStage
import com.example.echolex.core.domain.data.model.lesson.RepeatingStage
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont


@Composable
fun StageItemCreating(modifier: Modifier = Modifier, stage: LessonStage, onClick: () -> Unit) {
    val text = when (stage) {
        is LearningStage -> stringResource(R.string.learn)
        is RepeatingStage -> stringResource(R.string.repeat)
    }

    StageItemContainer(
        modifier = modifier.size(40.dp),
        clickable = true,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.clickable(){
                onClick()
            }) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = nunitoVariableFont,
                    fontWeight = FontWeight.Black,
                    color = AppContentColor
                )
            )
        }
    }
}

@Composable
fun StageItemContainer(
    modifier: Modifier = Modifier,
    cornerDp: Dp = 5.dp,
    borderColor: Color = AppContentColor,
    backgroundColor: Color = AppButtonBackgroundColor,
    clickable: Boolean = false,
    onClick: (() -> Unit)? = null,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    val baseModifier = modifier
        .clip(RoundedCornerShape(cornerDp))
        .border(1.dp, borderColor, RoundedCornerShape(cornerDp))
        .background(backgroundColor)
        .then(if (clickable && onClick != null) Modifier.clickable { onClick() } else Modifier)
    Box(
        modifier = baseModifier,
        contentAlignment = contentAlignment
    ) {
        content()
    }
}

@Composable
fun StageCreatingButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    StageItemContainer(
        modifier = modifier.size(40.dp),
        clickable = true,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_stage),
            tint = AppContentColor,
            modifier = Modifier
                .size(30.dp)
                .padding(5.dp)
        )
    }
}