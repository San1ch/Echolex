package com.example.echolex.ui.screens.MainScreen.LessonMenuScreen.item

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.R
import com.example.echolex.ui.customDesign.ItemRowBackground
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun LessonItem(
    onDelete: () -> Unit,
    lessonName: String,
    chooseLesson: () -> Unit,
    onStartLearning: () -> Unit,
    isSelected: Boolean
) {

    val animatedContentColor by animateColorAsState(
        targetValue = if (isSelected) AppButtonBackgroundColor else AppButtonContentColor,
        label = "borderAnim"
    )
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) AppButtonContentColor else AppButtonBackgroundColor,
        label = "backgroundAnim"
    )

    ItemRowBackground(
        backgroundColor = animatedBackgroundColor,
        contentColor = animatedContentColor,
        onBackgroundClick = {
            chooseLesson()
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = lessonName,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = nunitoVariableFont,
                    color = animatedContentColor
                ),
                modifier = Modifier
                    .wrapContentSize()
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onStartLearning,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Почати навчання",
                        tint = animatedContentColor,
                        modifier = Modifier.size(25.dp)
                    )
                }
                
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = animatedContentColor,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
}