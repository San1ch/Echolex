package com.example.echolex.core.domain.useCase.deckStore

import com.example.echolex.core.data.model.AppNotification
import com.example.echolex.core.data.model.dataclass.Card
import com.example.echolex.core.data.model.dataclass.Deck
import com.example.echolex.core.domain.service.DataDeck
import com.example.echolex.core.domain.validation.CorrectSymbolsValidationProvider
import com.example.echolex.core.data.repository.DeckMemoryStore
import com.example.echolex.core.domain.service.centralScreenService.NotificationCenter
import com.example.echolex.core.domain.validation.ImportCardStringValidationCorrectCommaCount
import com.example.echolex.core.domain.validation.ImportCardStringValidationNoDigits
import com.example.echolex.core.domain.validation.ImportCardStringValidationNoEmptyWords
import com.example.echolex.core.domain.validation.ImportCardStringValidationNotEmpty
import com.example.echolex.core.domain.useCase.CardParserService
import javax.inject.Inject

class AddCardsToDeckUseCase @Inject constructor(
    private val parserService: CardParserService,
    private val deckMemoryStore: DeckMemoryStore,
    private val notificationCenter: NotificationCenter
) {
    suspend operator fun invoke(nameDeckToAdd: String, dataDeck: DataDeck): Boolean{
        val foundDeck = deckMemoryStore.getDeckByName(nameDeckToAdd)
        if(foundDeck == null){
            notificationCenter.setNotification(AppNotification.Business.SameDeckName)
            return false
        }

        var notification = validate(dataDeck)
        if(notification != AppNotification.Null){
            notificationCenter.setNotification(notification)
            return false
        }

        val cards = parserService.parseCards(dataDeck)
        val sameCardName = findingSameCard(cards, foundDeck.cards)
        if(sameCardName.isNotEmpty()) {
            AppNotification.Business.SameCards(sameCardName)
            return false
        }

        deckMemoryStore.updateDeck(Deck(nameDeckToAdd, cards + foundDeck.cards))

        notificationCenter.setNotification(AppNotification.Null)
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

    private fun validate(data: DataDeck): AppNotification{
        val validationProvider = CorrectSymbolsValidationProvider(listOf(
            ImportCardStringValidationNotEmpty(),
            ImportCardStringValidationCorrectCommaCount(),
            ImportCardStringValidationNoEmptyWords(),
            ImportCardStringValidationNoDigits()
        ))
        return validationProvider.validate(data)
    }
}