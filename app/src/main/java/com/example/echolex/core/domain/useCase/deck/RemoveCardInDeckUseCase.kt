package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.DeckFindResult
import com.example.echolex.core.domain.data.repository.DeckMemoryStore
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveCardInDeckUseCase @Inject constructor(
    private val deckMemoryStore: DeckMemoryStore,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {
    suspend operator fun invoke(deckName: String, card: Card) {
        withContext(Dispatchers.IO) {
            val deckResult = deckMemoryStore.getDeckByName(deckName)

            when (deckResult) {
                is DeckFindResult.NotFound -> {
                    openAppNotificationUseCase(AppNotification.Business.DeckDoesNotExist)
                    return@withContext
                }

                is DeckFindResult.Success -> {
                    val deck = deckResult.deck
                    val needCard = deck.cards.find { it == card }
                    if (needCard == null) {
                        openAppNotificationUseCase(AppNotification.Business.CardDoesNotExist)
                        return@withContext
                    }

                    val updatedDeck = deck.copy(cards = deck.cards.filter { it != needCard })

                    deckMemoryStore.updateDeck(updatedDeck)

                    openAppNotificationUseCase(AppNotification.Null)
                }
            }

        }
    }
}
