package com.example.echolex.ui.customDesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.ui.theme.AppButtonBorderColor
import com.example.echolex.ui.theme.AppButtonContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun AppOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    singleLine: Boolean = true,
    backgroundColor: Color = Color.Transparent,
    borderColor: Color = AppButtonBorderColor,
    placeholderColor: Color = AppButtonContentColor,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = nunitoVariableFont,
        color = AppButtonContentColor
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        placeholder = {
            Text(
                placeholderText,
                style = textStyle.copy(color = placeholderColor)
            )
        },
        textStyle = textStyle,
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = borderColor,
            errorContainerColor = borderColor,

            cursorColor = borderColor,
            errorCursorColor = borderColor,

            focusedIndicatorColor = borderColor,
            unfocusedIndicatorColor = borderColor,
            disabledIndicatorColor = borderColor,
            errorIndicatorColor = borderColor,

        ),
                shape = RoundedCornerShape(8.dp),
    )
}