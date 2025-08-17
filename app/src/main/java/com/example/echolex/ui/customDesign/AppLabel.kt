package com.example.echolex.ui.customDesign

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun AppLabel(
    text: String,
    modifier: Modifier = Modifier,
    startPadding: Int = 20,
    textColor: Color = AppContentColor
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 15.sp,
            fontFamily = nunitoVariableFont,
            fontWeight = FontWeight.Medium,
            color = textColor
        ),
        modifier = modifier.padding(start = startPadding.dp)
    )
}
