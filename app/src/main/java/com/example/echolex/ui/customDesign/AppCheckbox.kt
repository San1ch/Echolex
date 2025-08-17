package com.example.echolex.ui.customDesign

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.echolex.ui.theme.AppContentBlackColor
import com.example.echolex.ui.theme.AppContentColor

@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorChecked: Color = AppContentColor,
    colorUnchecked: Color = AppContentColor,
    colorCheckmark: Color = AppContentBlackColor,
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = colorChecked,
            uncheckedColor = colorUnchecked,
            checkmarkColor = colorCheckmark,

        )
    )
}