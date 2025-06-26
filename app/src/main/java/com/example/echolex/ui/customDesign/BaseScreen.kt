package com.example.echolex.ui.customDesign

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StandardStart(padding: Int = 15, content: @Composable () -> Unit) {
    Box {
        StandardAppBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding.dp)
        ) {
            content()
        }
    }
}