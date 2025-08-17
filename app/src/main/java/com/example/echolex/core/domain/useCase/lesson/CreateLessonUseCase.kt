package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.model.lesson.LessonConfig
import com.example.echolex.core.domain.data.repository.DeckMemoryStore
import com.example.echolex.core.domain.data.repository.DeckFindResult
import com.example.echolex.core.domain.data.repository.LessonMemoryStore
import javax.inject.Inject

class CreateLessonUseCase @Inject constructor(
    private val lessonMemoryStore: LessonMemoryStore,
    private val deckMemoryStore: DeckMemoryStore,
    private val validateLessonUseCase: ValidateLessonUseCase,
    private val checkSimilarLessonUseCase: CheckSimilarLessonUseCase
) {
    operator fun invoke(blueprint: LessonBlueprint, deckNames: List<String>, name: String): Boolean {
        if (checkSimilarLessonUseCase(name)) {
            return false
        }

        // Отримуємо всі карти з вказаних колод
        val allCards = mutableListOf<com.example.echolex.core.domain.data.model.deck.Card>()
        for (deckName in deckNames) {
            val deckResult = deckMemoryStore.getDeckByName(deckName)
            if (deckResult is DeckFindResult.Success) {
                allCards.addAll(deckResult.deck.cards)
            }
        }

        val lesson = Lesson(
            name = name,
            config = LessonConfig(
                deckNames = deckNames,
                stages = blueprint.stages,
                cards = allCards,
                settings = blueprint.settings
            )
        )

        if (!validateLessonUseCase(lesson)) {
            return false
        }
        
        lessonMemoryStore.addLesson(lesson)
        return true
    }
}