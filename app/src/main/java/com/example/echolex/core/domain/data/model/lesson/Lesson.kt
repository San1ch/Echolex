package com.example.echolex.core.domain.data.model.lesson

import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.model.lesson.CardSelectionMode
import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val name: String,
    val config: LessonConfig,
    val currentStageIndex: Int = 0,
    val currentCardIndex: Int = 0,
    val completedCards: Set<String> = emptySet()
) {
    fun getNextCard(): Card? {
        val currentStage = config.stages.getOrNull(currentStageIndex) ?: return null
        val availableCards = getAvailableCardsForStage(currentStage)
        
        return availableCards.getOrNull(currentCardIndex)
    }
    
    fun markCardAsCompleted(card: Card): Lesson {
        val cardId = "${card.firstWord}_${card.secondWord}"
        return copy(completedCards = completedCards + cardId)
    }
    
    fun moveToNextCard(): Lesson {
        val currentStage = config.stages.getOrNull(currentStageIndex) ?: return this
        val availableCards = getAvailableCardsForStage(currentStage)
        
        return if (currentCardIndex < availableCards.size - 1) {
            copy(currentCardIndex = currentCardIndex + 1)
        } else {
            moveToNextStage()
        }
    }
    
    private fun moveToNextStage(): Lesson {
        return if (currentStageIndex < config.stages.size - 1) {
            copy(
                currentStageIndex = currentStageIndex + 1,
                currentCardIndex = 0
            )
        } else {
            this // Урок завершено
        }
    }
    
    private fun getAvailableCardsForStage(stage: LessonStage): List<Card> {
        return when (stage.type) {
            StageType.LEARNING -> config.cards.take(stage.cards)
            StageType.REPEATING -> config.cards.filter { it.repeatingCount >= stage.priority }
        }
    }
    
    fun isCompleted(): Boolean {
        return currentStageIndex >= config.stages.size
    }
}

@Serializable
data class LessonConfig(
    val deckNames: List<String>,
    val stages: List<LessonStage>,
    val cards: List<Card>,
    val settings: LessonSettings
)

@Serializable
data class LessonStage(
    val type: StageType,
    val cards: Int = 10,
    val cycles: Int = 5,
    val priority: Int = 0,
    val cardSelectionMode: CardSelectionMode = CardSelectionMode.RANDOM
)

@Serializable
enum class StageType {
    LEARNING, REPEATING
}

@Serializable
data class LessonSettings(
    val isLoop: Boolean = false
)
