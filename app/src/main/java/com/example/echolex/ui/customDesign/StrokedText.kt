package com.example.echolex.ui.customDesign

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonContentColor
import com.example.echolex.ui.theme.fugazOneFontFamily

@Composable
fun AppStrokedText(
    text: String,
    fontSize: TextUnit = 20.sp,
    strokeColor: Color = AppButtonBackgroundColor,
    textColor: Color = AppButtonContentColor,
    strokeWidth: Float = 4f,
    fontFamily: FontFamily = fugazOneFontFamily,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = fontFamily,
            color = strokeColor,
            style = TextStyle(
                shadow = Shadow(
                    color = strokeColor,
                    offset = Offset(0f, 0f),
                    blurRadius = strokeWidth
                )
            )
        )
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = fontFamily,
            color = textColor
        )
    }
}