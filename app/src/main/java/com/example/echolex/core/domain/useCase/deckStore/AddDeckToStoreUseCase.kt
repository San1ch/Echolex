package com.example.echolex.core.domain.useCase.deckStore

import com.example.echolex.core.data.model.AppNotification
import com.example.echolex.core.data.model.dataclass.Deck
import com.example.echolex.core.data.repository.DeckMemoryStore
import javax.inject.Inject

class AddDeckToStoreUseCase @Inject constructor(
    val deckMemoryStore: DeckMemoryStore
) {
    suspend operator fun invoke(deck: Deck): AppNotification {
        if (!nameValidation(deck.name)) {
            return AppNotification.Business.SameDeckName
        }
        deckMemoryStore.addDeck(deck)
        return AppNotification.Null
    }

    private fun nameValidation(name: String): Boolean {
        if (name.length !in 3..30) return false
        if (!name.first().isLetter()) return false
        if (!name.all { it.isLetterOrDigit() || it.isWhitespace() }) return false
        return true
    }
}

