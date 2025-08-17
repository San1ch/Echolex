package com.example.echolex.ui.customDesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.echolex.ui.theme.AppStandardBackgroundColor

@Composable
fun StandardAppBackground() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    AppStandardBackgroundColor
                )
        ) {

        }
    }
}
