package com.example.echolex.core.domain.data.repository

import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.deck.Deck


fun Deck.toEntity(): DeckEntity = DeckEntity(name = name)

fun Card.toEntity(deckName: String): CardEntity = CardEntity(
    deckName = deckName,
    firstWord = firstWord,
    secondWord = secondWord,
    repeatingCount = repeatingCount,
    isPreLearned = isPreLearned
)

fun CardEntity.toDomain(): Card = Card(
    firstWord = firstWord,
    secondWord = secondWord,
    repeatingCount = repeatingCount,
    isPreLearned = isPreLearned
)

fun DeckWithCards.toDomain(): Deck = Deck(
    name = deck.name,
    cards = cards.map { it.toDomain() }
)