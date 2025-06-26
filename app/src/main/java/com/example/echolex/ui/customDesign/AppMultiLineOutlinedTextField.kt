package com.example.echolex.ui.customDesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
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
import com.example.echolex.ui.theme.AppButtonBackgroundColor
import com.example.echolex.ui.theme.AppButtonBorderColor
import com.example.echolex.ui.theme.AppButtonContentColor
import com.example.echolex.ui.theme.nunitoVariableFont

@Composable
fun AppMultiLineOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    backgroundColor: Color = AppButtonBackgroundColor,
    borderColor: Color = AppButtonBorderColor,
    placeholderColor: Color = AppButtonContentColor,
    textStyle: TextStyle = TextStyle(
        fontSize = 15.sp,
        fontFamily = nunitoVariableFont,
        color = AppButtonContentColor
    ),
    minLines: Int = 3,
    maxLines: Int = 6,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = false,
        minLines = minLines,
        maxLines = maxLines,
        placeholder = {
            Text(
                text = placeholderText,
                style = textStyle.copy(color = placeholderColor)
            )
        },
        textStyle = textStyle,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(10.dp)).height(200.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            errorContainerColor = backgroundColor,

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
