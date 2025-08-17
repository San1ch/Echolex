package com.example.echolex.ui.customDesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonContentColor

@Composable
fun ItemRowBackground(
    padding: Dp = 8.dp,
    backgroundColor: Color = AppButtonBackgroundColor,
    contentColor: Color = AppButtonContentColor,
    onBackgroundClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = contentColor,
                shape = RoundedCornerShape(8.dp)
            )
            .then(
                if (onBackgroundClick != null)
                    Modifier.clickable { onBackgroundClick() }
                else Modifier
            ),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding * 3, padding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                content = { content() }
            )
        }
    )
}
