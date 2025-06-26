package com.example.echolex.ui.customDesign

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun AppSliderLabelCount(
    modifier: Modifier = Modifier,
    text: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    min: Float,
    max: Float,
    steps: Int = 0,
) {
    Column(modifier = modifier.padding(10.dp).wrapContentHeight().fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 15.dp, vertical = 0.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp, bottom = 3.dp),
                text = text, style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppContentColor
                )
            )
            Text(
                modifier = Modifier,
                text = value.toInt().toString(), style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppContentColor
                )
            )
        }
        AppSlider(
            value = value,
            onValueChange = { onValueChange(it) },
            min = min,
            max = max,
            steps = steps,
        )
    }
}

@Composable
fun AppSlider(modifier: Modifier = Modifier, value: Float, onValueChange: (Float) -> Unit, min: Float, max: Float, steps: Int = 0) {
    Slider(value = value, onValueChange = onValueChange, valueRange = min..max, steps = steps, modifier = modifier, colors = SliderDefaults.colors(
            thumbColor = AppContentColor,
            activeTrackColor = AppContentColor,
            inactiveTrackColor = AppContentColor.copy(alpha = 0.2f)
        ))
}
