package com.example.echolex.core.domain.useCase.deckStore

import com.example.echolex.core.data.model.AppNotification
import com.example.echolex.core.data.repository.DeckMemoryStore
import com.example.echolex.core.domain.service.DataDeck
import com.example.echolex.core.domain.service.centralScreenService.NotificationCenter
import com.example.echolex.core.domain.validation.CorrectSymbolsValidationProvider
import com.example.echolex.core.domain.validation.DeckNameCharacterValidation
import com.example.echolex.core.domain.validation.DeckNameLengthValidation
import com.example.echolex.core.domain.validation.DeckNameStartCharValidation
import javax.inject.Inject

class ChangeDeckNameUseCase @Inject constructor(
   private val deckMemoryStore: DeckMemoryStore,
    private val notificationCenter: NotificationCenter
) {
    suspend operator fun invoke(newName: String, oldName: String): Boolean {
        if(newName == oldName){
            notificationCenter.setNotification(AppNotification.Business.SameDeckName)
            return false
        }

        val oldDeck = deckMemoryStore.getDeckByName(oldName)
        if(oldDeck == null){
            notificationCenter.setNotification(AppNotification.Business.DeckDoesNotExist)
            return false
        }

        val notification = validate(DataDeck(name = newName, words = "", isMarkedPreLearning = false))
        if (notification != AppNotification.Null) {
            notificationCenter.setNotification(notification)
            return false
        }

        val deckWithNewName = oldDeck.copy(name = newName)

        deckMemoryStore.changeDeckName(oldName, deckWithNewName)

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