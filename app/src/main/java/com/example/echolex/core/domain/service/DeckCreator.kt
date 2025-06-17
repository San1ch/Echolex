package com.example.echolex.core.domain.service

import com.example.echolex.core.data.model.dataclass.Card
import com.example.echolex.core.data.model.dataclass.Deck
import javax.inject.Qualifier
import javax.inject.Inject

interface DeckCreator {
    fun createDeck(data: DataDeck): Deck
}

class DeckEmptyCreator @Inject constructor(
) : DeckCreator {
    override fun createDeck(data: DataDeck): Deck {
        return Deck(data.name, listOf())
    }
}

class DeckImportCreator @Inject constructor(
) : DeckCreator {

    override fun createDeck(data: DataDeck): Deck {
        return Deck(data.name, unzipCards(data))
    }

    private fun unzipCards(data: DataDeck): List<Card> {
        return data.words
            .split(";")
            .mapNotNull { line ->
                val parts = line.trim().split(",")
                if (parts.size == 2) {
                    val word = parts[0].trim()
                    val translation = parts[1].trim()
                    if (word.isNotEmpty() && translation.isNotEmpty()) {
                        Card(
                            firstWord = word,
                            secondWord = translation,
                            isPreLearnedCard = data.isMarkedPreLearning
                        )
                    } else null
                } else null
            }
    }
}

data class DataDeck(
    val name: String,
    val words: String,
    val isMarkedPreLearning: Boolean
)

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DeckEmptyCreatorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DeckImportCreatorQualifier
