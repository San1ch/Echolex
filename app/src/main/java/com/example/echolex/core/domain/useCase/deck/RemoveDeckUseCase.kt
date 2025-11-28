package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.DeckFindResult
import com.example.echolex.core.domain.data.repository.DeckRepository
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class RemoveDeckUseCase @Inject constructor(
    val deckRepository: DeckRepository,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {
    suspend operator fun invoke(name: String){
        val foundDeck = deckRepository.getDeckByName(name)
        if(foundDeck is DeckFindResult.NotFound){
            return openAppNotificationUseCase(AppNotification.Business.DeckDoesNotExist)
        }
        deckRepository.removeDeckByName(name)
        return openAppNotificationUseCase(AppNotification.Null)
    }
}