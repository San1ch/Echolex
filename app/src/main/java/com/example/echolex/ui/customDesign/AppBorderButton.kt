package com.example.echolex.ui.customDesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonBorderColor
import com.example.echolex.ui.theme.AppButtonContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun AppBorderButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = AppButtonBackgroundColor,
    borderColor: Color = AppButtonBorderColor,
    borderWidth: Dp = 1.dp,
    modifier: Modifier = Modifier,
    textSize: Int = 25
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(backgroundColor)
            .border(borderWidth, borderColor, RoundedCornerShape(15.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = textSize.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = nunitoVariableFont,
                color = AppButtonContentColor
            )
        )
    }
}