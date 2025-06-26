package com.example.echolex.ui.customDesign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DualButtonRow(
    modifier: Modifier = Modifier,
    leftButtonText: String,
    rightButtonText: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    leftButtonModifier: Modifier = Modifier,
    rightButtonModifier: Modifier = Modifier,
    spacing: Dp = 20.dp
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppBorderButton(
            text = leftButtonText,
            modifier = leftButtonModifier
                .fillMaxWidth()
                .weight(1f),
            onClick = onLeftClick
        )

        Spacer(modifier = Modifier.width(spacing))

        AppBorderButton(
            text = rightButtonText,
            modifier = rightButtonModifier
                .fillMaxWidth()
                .weight(1f),
            onClick = onRightClick
        )
    }
}
