package com.example.echolex.core.domain.useCase

import com.example.echolex.core.data.model.AppNotification
import com.example.echolex.core.data.model.dataclass.Card
import com.example.echolex.core.data.model.dataclass.Deck
import com.example.echolex.core.domain.service.DataDeck
import com.example.echolex.core.domain.service.centralScreenService.NotificationCenter
import com.example.echolex.core.domain.validation.CorrectSymbolsValidationProvider
import com.example.echolex.core.domain.validation.DeckNameCharacterValidation
import com.example.echolex.core.domain.validation.DeckNameLengthValidation
import com.example.echolex.core.domain.validation.DeckNameStartCharValidation
import com.example.echolex.core.domain.validation.ImportCardStringValidationCorrectCommaCount
import com.example.echolex.core.domain.validation.ImportCardStringValidationNoDigits
import com.example.echolex.core.domain.validation.ImportCardStringValidationNoEmptyWords
import com.example.echolex.core.domain.validation.ImportCardStringValidationNotEmpty
import com.example.echolex.core.domain.useCase.deckStore.AddDeckToStoreUseCase
import com.example.echolex.core.domain.useCase.deckStore.GetDeckByNameUseCase
import com.example.echolex.core.domain.validation.ImportCardStringValidationNoSpecialSymbols
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateEmptyDeckUseCase @Inject constructor(
    val addDeckToStoreUseCase: AddDeckToStoreUseCase,
    val checkSameDeckUseCase: CheckSameDeckUseCase,
    val notificationCenter: NotificationCenter
) {
    suspend operator fun invoke(data: DataDeck): Boolean {
        var notification = validate(data)
        if (notification != AppNotification.Null){
            notificationCenter.setNotification(notification)
            return false
        }

        notification = checkSameDeckUseCase(data.name)
        if (notification != AppNotification.Null){
            notificationCenter.setNotification(notification)
            return false
        }

        addDeckToStoreUseCase(Deck(data.name))

        notificationCenter.setNotification(AppNotification.Null)
        return true
    }

    fun validate(data: DataDeck): AppNotification {
        val validationProvider = CorrectSymbolsValidationProvider(listOf(
            DeckNameLengthValidation(),
            DeckNameStartCharValidation(),
            DeckNameCharacterValidation()
        ))

        return validationProvider.validate(data)
    }
}

class CreateImportDeckUseCase @Inject constructor(
    private val addDeckToStoreUseCase: AddDeckToStoreUseCase,
    private val checkSameDeckUseCase: CheckSameDeckUseCase,
    private val cardParserService: CardParserService,
    private val notificationCenter: NotificationCenter
) {
    suspend operator fun invoke(data: DataDeck): Boolean {
        val notification = validate(data)
        if (notification != AppNotification.Null) {
            notificationCenter.setNotification(notification)
            return false
        }

        val duplicateCheck = checkSameDeckUseCase(data.name)
        if (duplicateCheck != AppNotification.Null) {
            notificationCenter.setNotification(duplicateCheck)
            return false
        }

        val cards: List<Card> = cardParserService.parseCards(data)
        addDeckToStoreUseCase(Deck(data.name, cards))

        notificationCenter.setNotification(AppNotification.Null)
        return true
    }

    fun validate(data: DataDeck): AppNotification {
        val validationProvider = CorrectSymbolsValidationProvider(
            listOf(
                DeckNameLengthValidation(),
                DeckNameStartCharValidation(),
                DeckNameCharacterValidation(),
                ImportCardStringValidationNotEmpty(),
                ImportCardStringValidationCorrectCommaCount(),
                ImportCardStringValidationNoEmptyWords(),
                ImportCardStringValidationNoDigits(),
                ImportCardStringValidationNoSpecialSymbols()
            )
        )

        return validationProvider.validate(data)
    }
}


class CheckSameDeckUseCase @Inject constructor(
    val getDeckByNameUseCase: GetDeckByNameUseCase
){
    suspend operator fun invoke(name: String): AppNotification{
        val foundDeck: Deck = getDeckByNameUseCase(name).first()
        if(foundDeck.name!= ""){
            return AppNotification.Business.SameDeckName
        }
        return AppNotification.Null
    }
}

class CardParserService @Inject constructor() {
    suspend fun parseCards(data: DataDeck): List<Card> {
        return data.words
            .split(";")
            .mapNotNull { line ->
                val parts = line.trim().split(",")
                if (parts.size == 2) {
                    val word = parts[0].trim()
                    val translation = parts[1].trim()
                    if (word.isNotEmpty() && translation.isNotEmpty()) {
                        Card(
                            firstWord = word,
                            secondWord = translation,
                            isPreLearnedCard = data.isMarkedPreLearning
                        )
                    } else null
                } else null
            }
    }
}