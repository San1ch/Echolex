package com.example.echolex.ui.customDesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.ui.theme.AppBlackGradientColor
import com.example.echolex.ui.theme.AppLightGradientColor

@Composable
fun StandardAppBackground() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.sp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(AppBlackGradientColor, AppLightGradientColor),
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 1000f)
                    )
                )
        )

    }
}