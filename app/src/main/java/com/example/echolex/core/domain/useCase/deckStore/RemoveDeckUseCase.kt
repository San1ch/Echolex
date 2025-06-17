package com.example.echolex.core.domain.useCase.deckStore

import com.example.echolex.core.data.model.AppNotification
import com.example.echolex.core.data.repository.DeckMemoryStore
import com.example.echolex.core.domain.service.centralScreenService.NotificationCenter
import javax.inject.Inject

class RemoveDeckUseCase @Inject constructor(
    val deckMemoryStore: DeckMemoryStore,
    val notificationCenter: NotificationCenter
) {
    suspend operator fun invoke(name: String){
        val foundDeck = deckMemoryStore.getDeckByName(name)
        if(foundDeck == null){
            return notificationCenter.setNotification(AppNotification.Business.DeckDoesNotExist)
        }
        deckMemoryStore.removeDeckByName(name)
        return notificationCenter.setNotification(AppNotification.Null)
    }
}