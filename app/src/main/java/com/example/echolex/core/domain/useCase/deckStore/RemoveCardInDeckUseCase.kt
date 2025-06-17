package com.example.echolex.core.domain.useCase.deckStore

import com.example.echolex.core.data.model.AppNotification
import com.example.echolex.core.data.model.dataclass.Card
import com.example.echolex.core.data.repository.DeckMemoryStore
import com.example.echolex.core.domain.service.centralScreenService.NotificationCenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveCardInDeckUseCase @Inject constructor(
    private val deckMemoryStore: DeckMemoryStore,
    private val notificationCenter: NotificationCenter
) {
    suspend operator fun invoke(deckName: String, card: Card?) {
        withContext(Dispatchers.IO) {
            val deck = deckMemoryStore.getDeckByName(deckName)
            if (deck == null) {
                notificationCenter.setNotification(AppNotification.Business.DeckDoesNotExist)
                return@withContext
            }

            val needCard = deck.cards.find { it == card }
            if (needCard == null) {
                notificationCenter.setNotification(AppNotification.Business.CardDoesNotExist)
                return@withContext
            }

            val updatedDeck = deck.copy(cards = deck.cards.filter { it != needCard })

            deckMemoryStore.updateDeck(updatedDeck)

            notificationCenter.setNotification(AppNotification.Null)
        }
    }
}
