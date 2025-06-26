package com.example.echolex.ui.customDesign

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.ui.theme.AppContentAltColor
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun LabeledCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorChecked: Color = AppContentColor,
    colorUnchecked: Color = AppContentColor,
    colorCheckmark: Color = AppContentAltColor,
    textStyle: TextStyle = TextStyle(
        fontSize = 15.sp,
        fontFamily = nunitoVariableFont,
        color = AppContentAltColor
    ),
    spacing: Dp = 5.dp,
    checkboxModifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppCheckbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = checkboxModifier,
            enabled = enabled,
            colorChecked = colorChecked,
            colorUnchecked = colorUnchecked,
            colorCheckmark = colorCheckmark
        )
        Spacer(modifier = Modifier.width(spacing))
        Text(text, style = textStyle)
    }
}
