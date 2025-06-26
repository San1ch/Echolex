package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.DeckMemoryStore
import javax.inject.Inject

class AddDeckToStoreUseCase @Inject constructor(
    private val deckMemoryStore: DeckMemoryStore,
    private val deckExistsByNameUseCase: DeckExistsByNameUseCase
) {
    suspend operator fun invoke(deck: Deck): AppNotification {
        if (deckExistsByNameUseCase(deck.name)) {
            return AppNotification.Business.SameDeckName
        }
        deckMemoryStore.addDeck(deck)
        return AppNotification.Null
    }
}

