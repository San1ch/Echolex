package com.example.echolex.ui.screens.MainScreen.LessonSettingsScreen.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.echolex.ui.theme.AppContentColor
import com.example.echolex.ui.theme.AppTransparencyColor

@Composable
fun BlueprintItemCreating(onClick: () -> Unit) {
    val cornerAndBorder = 5
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(cornerAndBorder.dp))
            .border(1.dp, AppContentColor, RoundedCornerShape(cornerAndBorder.dp))
            .background(AppTransparencyColor)
    ) {

    }
    Spacer(
        modifier = Modifier
            .height(cornerAndBorder.dp)
    )
}