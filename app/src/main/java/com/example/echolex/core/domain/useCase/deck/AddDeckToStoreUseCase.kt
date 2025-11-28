package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.DeckRepository
import javax.inject.Inject

class AddDeckToStoreUseCase @Inject constructor(
    private val deckRepository: DeckRepository,
    private val deckExistsByNameUseCase: DeckExistsByNameUseCase
) {
    suspend operator fun invoke(deck: Deck): AppNotification {
        if (deckExistsByNameUseCase(deck.name)) {
            return AppNotification.Business.SameDeckName
        }
        deckRepository.addDeck(deck)
        return AppNotification.Null
    }
}

