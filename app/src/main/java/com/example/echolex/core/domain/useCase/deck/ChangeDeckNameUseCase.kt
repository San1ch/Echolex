package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.DeckFindResult
import com.example.echolex.core.domain.data.repository.DeckMemoryStore
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class ChangeDeckNameUseCase @Inject constructor(
   private val deckMemoryStore: DeckMemoryStore,
   private val openAppNotificationUseCase: OpenAppNotificationUseCase,
   private val validate: ValidateDeckNameUseCase
) {
    suspend operator fun invoke(newName: String, oldName: String): Boolean {
        if(newName == oldName){
            openAppNotificationUseCase(AppNotification.Business.SameDeckName)
            return false
        }

        val oldDeckResult = deckMemoryStore.getDeckByName(oldName)
        when(oldDeckResult) {
            is DeckFindResult.NotFound -> {
                openAppNotificationUseCase(AppNotification.Business.DeckDoesNotExist)
                return false
            }
            is DeckFindResult.Success -> {
                val oldDeck = oldDeckResult.deck

                val notification = validate(newName)
                if (notification != AppNotification.Null) {
                    openAppNotificationUseCase(notification)
                    return false
                }

                val deckWithNewName = oldDeck.copy(name = newName)

                deckMemoryStore.changeDeckName(oldName, deckWithNewName)

                openAppNotificationUseCase(AppNotification.Null)
                return true
            }
        }
    }
}