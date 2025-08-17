package com.example.echolex.ui.customDesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.AppTransparencyColor

@Composable
fun AppStandardDialogBackground(
    onOverlayClick: (() -> Unit)? = null,
    onDialogClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTransparencyColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onOverlayClick?.invoke() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AppButtonBackgroundColor)
                .then(
                    if (onDialogClick != null) Modifier.clickable(onClick = onDialogClick)
                    else Modifier
                )
        ) {
            content()
        }
    }
}


