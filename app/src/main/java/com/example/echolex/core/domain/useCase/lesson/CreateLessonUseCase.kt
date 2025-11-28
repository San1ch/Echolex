package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.model.lesson.LessonParameters
import com.example.echolex.core.domain.data.model.lesson.LessonStageData
import com.example.echolex.core.domain.data.repository.DeckRepository
import com.example.echolex.core.domain.data.repository.DeckFindResult
import com.example.echolex.core.domain.data.repository.LessonRepository
import com.example.echolex.core.domain.useCase.deck.GetCardsByDeckNamesAndParametersUseCase
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.IncrementRepeatedCardsUseCase
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.MarkCardsAsPreLearnedUseCase
import javax.inject.Inject

class CreateLessonUseCase @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val deckRepository: DeckRepository,
    private val validateLessonUseCase: ValidateLessonUseCase,
    private val checkSimilarLessonUseCase: CheckSimilarLessonUseCase,
    private val getNextCards: GetCardsByDeckNamesAndParametersUseCase,
    private val incrementRepeatedCardsUseCase: IncrementRepeatedCardsUseCase,
    private val markCardsAsPreLearnedUseCase: MarkCardsAsPreLearnedUseCase
) {
    suspend operator fun invoke(lessonBlueprint: LessonBlueprint, deckNames: List<String>, name: String): Boolean {
        if (checkSimilarLessonUseCase(name)) {
            return false
        }

        // Отримуємо всі карти з вказаних колод
        val allCards = mutableListOf<Card>()
        for (deckName in deckNames) {
            val deckResult = deckRepository.getDeckByName(deckName)
            if (deckResult is DeckFindResult.Success) {
                allCards.addAll(deckResult.deck.cards)
            }
        }

        val lesson = Lesson(
            name = name,
            parameters = LessonParameters(
                deckNames = deckNames,
                stages = lessonBlueprint.stages,
                settings = lessonBlueprint.settings
            ),
            changeableData = LessonStageData(
                type = lessonBlueprint.stages[0].type,
                cards = allCards,
                cycles = lessonBlueprint.stages[0].cycles,
                priority = lessonBlueprint.stages[0].priority,
                cardSelectionMode = lessonBlueprint.stages[0].cardSelectionMode,
                wasIncorrectAnswer = false
            ),
            getNextCards = getNextCards,
            incrementRepeatedCardsUseCase = incrementRepeatedCardsUseCase,
            markCardsAsPreLearnedUseCase = markCardsAsPreLearnedUseCase
        )

        if (!validateLessonUseCase(lesson)) {
            return false
        }
        
        lessonRepository.upsert(lesson)
        return true
    }
}