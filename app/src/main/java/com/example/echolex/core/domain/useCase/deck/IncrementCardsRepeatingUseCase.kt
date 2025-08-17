package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.DeckFindResult
import com.example.echolex.core.domain.data.repository.DeckMemoryStore
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class IncrementCardsRepeatingUseCase @Inject constructor(
    private val deckMemoryStore: DeckMemoryStore,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {
    suspend operator fun invoke(deckName: String, cardsToIncrement: List<Card>) {
        val deckResult = deckMemoryStore.getDeckByName(deckName)
        when (deckResult) {
            is DeckFindResult.Success -> {
                val deck = deckResult.deck
                val cardSet = cardsToIncrement.toSet()

                val updatedCards = deck.cards.map { card ->
                    if (card in cardSet) {
                        card.copy(repeatingCount = card.repeatingCount + 1)
                    } else card
                }

                deckMemoryStore.updateDeck(deck.copy(cards = updatedCards))
            }

            is DeckFindResult.NotFound -> {
                openAppNotificationUseCase(AppNotification.Business.DeckDoesNotExist)
                return
            }
        }
    }
}
