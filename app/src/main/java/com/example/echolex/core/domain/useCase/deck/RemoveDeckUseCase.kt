package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.DeckMemoryStore
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class RemoveDeckUseCase @Inject constructor(
    val deckMemoryStore: DeckMemoryStore,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {
    suspend operator fun invoke(name: String){
        val foundDeck = deckMemoryStore.getDeckByName(name)
        if(foundDeck == null){
            return openAppNotificationUseCase(AppNotification.Business.DeckDoesNotExist)
        }
        deckMemoryStore.removeDeckByName(name)
        return openAppNotificationUseCase(AppNotification.Null)
    }
}