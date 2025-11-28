package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.item

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun BlueprintItemInLessonCreating(
    blueprintName: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val animatedTextColor by animateColorAsState(
        targetValue = if (isSelected) AppButtonBackgroundColor else AppButtonContentColor,
        label = "borderAnim"
    )
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) AppButtonContentColor else AppButtonBackgroundColor,
        label = "backgroundAnim"
    )

    val cornerAndBorder = 5
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(cornerAndBorder.dp))
            .border(1.dp, AppButtonContentColor, RoundedCornerShape(cornerAndBorder.dp))
            .background(animatedBackgroundColor)
            .clickable() { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                blueprintName,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = nunitoVariableFont,
                    color = animatedTextColor,
                ),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 20.dp)
            )
        }
    }
    Spacer(
        modifier = Modifier
            .height(cornerAndBorder.dp)
    )
}