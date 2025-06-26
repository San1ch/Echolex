package com.example.echolex.ui.screens.MainScreen.LessonSettingsScreen.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolex.ui.customDesign.AppCheckbox
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.AppTransparencyColor
import com.example.echolex.ui.theme.nunitoVariableFont


@Composable
fun DeckItemInLessonCreating(deckName: String, countOfCards: String, onClick: () -> Unit) {
    val checkBoxState = remember { mutableStateOf(false) }
    val cornerAndBorder = 5
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(cornerAndBorder.dp))
            .border(1.dp, AppContentColor, RoundedCornerShape(cornerAndBorder.dp))
            .background(AppTransparencyColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            AppCheckbox(
                checked = checkBoxState.value,
                onCheckedChange = { checkBoxState.value = it },
            )
            Text(
                deckName, style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppContentColor
                )
            )

            Text(
                countOfCards, style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = nunitoVariableFont,
                    color = AppContentColor
                )
            )

        }

    }
    Spacer(
        modifier = Modifier
            .height(cornerAndBorder.dp)
    )
}

