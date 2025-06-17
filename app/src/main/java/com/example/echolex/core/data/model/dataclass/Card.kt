package com.example.echolex.core.data.model.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val firstWord: String,
    val secondWord: String,
    val countOfRepeating: Int = 0,
    val isPreLearnedCard: Boolean = false
) {
    fun flipCard(): Card {
        return copy(firstWord = secondWord, secondWord = firstWord)
    }
}