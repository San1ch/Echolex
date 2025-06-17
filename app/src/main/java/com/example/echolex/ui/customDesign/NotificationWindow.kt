package com.example.echolex.ui.customDesign

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonContentColor
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.fugazOneFontFamily

@Composable
fun NotificationWindow(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(
                onClick = onDismiss,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .width(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(AppButtonBackgroundColor)
                .clickable(
                    onClick = { },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Star",
                        modifier = Modifier.size(30.dp),
                        tint = AppContentColor
                    )
                    Spacer(modifier = Modifier
                        .width(5.dp))
                    Text(
                        text = title,
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontFamily = fugazOneFontFamily,
                            color = AppContentColor
                        )
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = description,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = fugazOneFontFamily,
                        color = AppButtonContentColor
                    ),
                    maxLines = 5
                )
            }
        }
    }
}