package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.repository.DeckFindResult
import com.example.echolex.core.domain.data.repository.DeckRepository
import com.example.echolex.core.domain.service.DataDeck
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class AddCardsToDeckUseCase @Inject constructor(
    private val parserService: CardParserService,
    private val deckRepository: DeckRepository,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase,
    private val validateDeckImportUseCase: ValidateDeckImportUseCase
) {
    suspend operator fun invoke(
        nameDeckToAdd: String,
        dataDeck: DataDeck,
        withFlipCards: Boolean
    ): Boolean {
        val foundDeckResult = deckRepository.getDeckByName(nameDeckToAdd)
        when (foundDeckResult) {
            is DeckFindResult.NotFound -> {
                openAppNotificationUseCase(AppNotification.Business.DeckDoesNotExist)
                return false
            }

            is DeckFindResult.Success -> {
                val foundDeck = foundDeckResult.deck

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
                val resultCards = if (withFlipCards) cards + cards.map {
                    Card(
                        firstWord = it.secondWord,
                        secondWord = it.firstWord
                    )
                }else {
                    cards
                }
                deckRepository.updateDeck(Deck(nameDeckToAdd, resultCards + foundDeck.cards))

                openAppNotificationUseCase(AppNotification.Null)
                return true
            }
        }
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