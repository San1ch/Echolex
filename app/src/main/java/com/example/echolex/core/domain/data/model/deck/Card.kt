package com.example.echolex.core.domain.data.model.deck

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val firstWord: String,
    val secondWord: String,
    val repeatingCount: Int = 0,
    val isPreLearned: Boolean = false
) {
    fun flipCard(): Card {
        return copy(firstWord = secondWord, secondWord = firstWord)
    }
}