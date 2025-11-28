package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.service.DataDeck
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateEmptyDeckUseCase @Inject constructor(
    val addDeckToStoreUseCase: AddDeckToStoreUseCase,
    val openAppNotificationUseCase: OpenAppNotificationUseCase,
    val validateDeckNameUseCase: ValidateDeckNameUseCase
) {
    suspend operator fun invoke(data: DataDeck): Boolean {
        var notification = validateDeckNameUseCase(data.name)
        if (notification != AppNotification.Null){
            openAppNotificationUseCase(notification)
            return false
        }

        if (notification != AppNotification.Null){
            openAppNotificationUseCase(notification)
            return false
        }

        addDeckToStoreUseCase(Deck(data.name))
        openAppNotificationUseCase(AppNotification.Null)
        return true
    }

}

class CreateImportDeckUseCase @Inject constructor(
    private val addDeckToStoreUseCase: AddDeckToStoreUseCase,
    private val checkSameDeckUseCase: CheckSameDeckUseCase,
    private val cardParserService: CardParserService,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase,
    private val validateDeckImportUseCase: ValidateDeckImportUseCase
) {
    suspend operator fun invoke(data: DataDeck): Boolean {
        val notification = validateDeckImportUseCase(data.words)
        if (notification != AppNotification.Null) {
            openAppNotificationUseCase(notification)
            return false
        }

        val duplicateCheck = checkSameDeckUseCase(data.name)
        if (duplicateCheck != AppNotification.Null) {
            openAppNotificationUseCase(duplicateCheck)
            return false
        }

        val cards: List<Card> = cardParserService.parseCards(data)
        addDeckToStoreUseCase(Deck(data.name, cards))

        openAppNotificationUseCase(AppNotification.Null)
        return true
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
            .lines()
            .map(String::trim)
            .filter(String::isNotEmpty)
            .mapNotNull { line ->
                val parts = line.split(",", limit = 2).map { it.trim() }
                val word = parts.getOrNull(0).orEmpty()
                val translation = parts.getOrNull(1).orEmpty()

                if (word.isNotEmpty() && translation.isNotEmpty()) {
                    Card(
                        firstWord = word,
                        secondWord = translation,
                        isPreLearned = data.isPreLearned
                    )
                } else null
            }
    }
}
