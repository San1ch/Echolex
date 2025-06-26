package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.repository.DeckMemoryStore
import com.example.echolex.core.domain.service.DataDeck
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import com.example.echolex.core.domain.useCase.validation.ValidateDeckImportUseCase
import javax.inject.Inject

class AddCardsToDeckUseCase @Inject constructor(
    private val parserService: CardParserService,
    private val deckMemoryStore: DeckMemoryStore,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase,
    private val validateDeckImportUseCase: ValidateDeckImportUseCase
) {
    suspend operator fun invoke(nameDeckToAdd: String, dataDeck: DataDeck): Boolean {
        val foundDeck = deckMemoryStore.getDeckByName(nameDeckToAdd)
        if (foundDeck == null) {
            openAppNotificationUseCase(AppNotification.Business.SameDeckName)
            return false
        }

        var notification = validateDeckImportUseCase(dataDeck.words)
        if (notification != AppNotification.Null) {
            openAppNotificationUseCase(notification)
            return false
        }

        val cards = parserService.parseCards(dataDeck)
        val sameCardName = findingSameCard(cards, foundDeck.cards)
        if (sameCardName.isNotEmpty()) {
            AppNotification.Business.SameCards(sameCardName)
            return false
        }

        deckMemoryStore.updateDeck(Deck(nameDeckToAdd, cards + foundDeck.cards))

        openAppNotificationUseCase(AppNotification.Null)
        return true
    }


    private fun findingSameCard(cards: List<Card>, cardsForAdd: List<Card>): String {
        cards.forEach { card ->
            if (cardsForAdd.any { it.firstWord == card.firstWord && it.secondWord == card.secondWord }) {
                return card.firstWord
            }
        }
        return ""
    }
}