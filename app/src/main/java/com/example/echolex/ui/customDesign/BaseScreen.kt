package com.example.echolex.ui.customDesign

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StandardStart(content: @Composable () -> Unit) {
    Box {
        StandardImageBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            content()
        }
    }
}