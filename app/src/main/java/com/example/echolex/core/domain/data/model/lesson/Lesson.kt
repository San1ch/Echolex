package com.example.echolex.core.domain.data.model.lesson

import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.useCase.deck.GetCardsByDeckNamesAndParametersUseCase
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.IncrementRepeatedCardsUseCase
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonLearningUiState
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.MarkCardsAsPreLearnedUseCase
import javax.inject.Inject

class Lesson @Inject constructor(
    val name: String,
    val parameters: LessonParameters,
    var changeableData: LessonStageData,
    @Transient private val getNextCards: GetCardsByDeckNamesAndParametersUseCase,

    @Transient private val incrementRepeatedCardsUseCase: IncrementRepeatedCardsUseCase,
    @Transient private val markCardsAsPreLearnedUseCase: MarkCardsAsPreLearnedUseCase,
) {
    internal var nextStageIndex: Int = 0
    internal var currentStageIndex: Int = -1

    /** Snapshot of the initial card set for the current stage (to replay on cycles/restart). */
    internal var currentStageCardList: List<Card> = emptyList()

    suspend fun start() {
        require(parameters.stages.isNotEmpty())
        if (currentStageIndex == -1) {
            val moved = advanceToNextStageOrNull()
            check(moved) { "Lesson has no stages to start." }
        }
    }

    /**
     * Returns:
     * - ui != null when there is a next card to show
     * - ui == null when lesson finished (notification will be provided)
     */
    suspend fun validation(wasIncorrectAnswer: Boolean): LessonStepResult {
        start()
        if (changeableData.wasIncorrectAnswer == false) {
            changeableData.wasIncorrectAnswer = wasIncorrectAnswer
        }

        var validator: LessonValidation = LessonValidationIsHaveCards()

        while (true) {
            when (val command = validator(this)) {

                is LessonValidationCommand.NextValidation -> validator = command.nextValidation

                LessonValidationCommand.NextCard -> return stepUi(notification = AppNotification.Null)

                LessonValidationCommand.NextCycle -> {
                    nextCycle()
                    validator = LessonValidationIsHaveCards()
                    return stepUi(notification = AppNotification.Lesson.LessonNextCycle)
                }

                LessonValidationCommand.RestartStage -> {
                    restartStage()
                    validator = LessonValidationIsHaveCards()
                    return stepUi(notification = AppNotification.Lesson.LessonStageRestarted)
                }

                LessonValidationCommand.NextStage -> {
                    if (changeableData.type == StageType.LEARNING) {
                        markCardsAsPreLearnedUseCase(
                            currentStageCardList,
                            parameters.deckNames
                        )
                    } else {
                        incrementRepeatedCardsUseCase(
                            currentStageCardList,
                            parameters.deckNames
                        )
                    }

                    advanceToNextStageOrNull()
                    validator = LessonValidationIsHaveCards()

                    val result = stepUi(notification = AppNotification.Lesson.LessonNextStage)
                    return result
                }

                LessonValidationCommand.Finished -> {
                    if (parameters.settings.isLoop) {

                        if (changeableData.type == StageType.LEARNING) {
                            markCardsAsPreLearnedUseCase(
                                currentStageCardList,
                                parameters.deckNames
                            )
                        } else {
                            incrementRepeatedCardsUseCase(
                                currentStageCardList,
                                parameters.deckNames
                            )
                        }

                        restartLesson()
                        validator = LessonValidationIsHaveCards()

                        val result = stepUi(notification = AppNotification.Lesson.LessonNextStage)
                        return LessonStepResult(
                            ui = result.ui,
                            notification = AppNotification.Lesson.LessonFinished,
                            context = changeableData
                        )
                    } else {
                        if (changeableData.type == StageType.LEARNING) {
                            markCardsAsPreLearnedUseCase(
                                currentStageCardList,
                                parameters.deckNames
                            )
                        } else {
                            incrementRepeatedCardsUseCase(
                                currentStageCardList,
                                parameters.deckNames
                            )
                        }
                        val result = LessonStepResult(
                            ui = null,
                            notification = AppNotification.Lesson.LessonFinished,
                            context = changeableData
                        )
                        restartLesson()
                        return result
                    }

                }
            }
        }
    }

    private fun stepUi(notification: AppNotification?): LessonStepResult {
        val ui = LessonLearningUiState(
            card = getNextCard(),
            remainingCards = changeableData.cards.size,
            remainingCycles = changeableData.cycles,
            wasIncorrect = changeableData.wasIncorrectAnswer,
            currentIndexStage = currentStageIndex,
            stageCount = parameters.stages.size,
        )
        return LessonStepResult(
            ui = ui, notification = notification,
            context = changeableData
        )
    }

    private fun getNextCard(): Card =
        changeableData.cards.removeAt(changeableData.cards.lastIndex)

    /**
     * Infinite repeats on mistakes: reset cycles to stage.cycles (your intended mastery-gating behavior).
     */
    private fun restartStage() {
        val stage = parameters.stages[currentStageIndex]
        changeableData = LessonStageData(
            type = stage.type,
            cards = currentStageCardList.shuffled().toMutableList(),
            cycles = stage.cycles,
            priority = stage.priority,
            cardSelectionMode = stage.cardSelectionMode,
            wasIncorrectAnswer = false
        )
    }

    private suspend fun restartLesson() {
        nextStageIndex = 0
        currentStageIndex = -1
        advanceToNextStageOrNull()
    }

    private suspend fun getCards(index: Int): List<Card> {
        return getNextCards(
            deckNames = parameters.deckNames,
            count = parameters.stages[index].cards,
            priority = parameters.stages[index].priority,
            cardSelectionMode = parameters.stages[index].cardSelectionMode,
            stageType = parameters.stages[index].type
        ).toList()
    }

    /**
     * Called only when cards are empty and cyclesLeft > 0.
     * Decrements cyclesLeft and reloads the same stage card set (shuffled).
     */
    private fun nextCycle() {
        changeableData.cycles--

        val stage = parameters.stages[currentStageIndex]
        changeableData = LessonStageData(
            type = stage.type,
            cards = currentStageCardList.shuffled().toMutableList(),
            cycles = changeableData.cycles,
            priority = stage.priority,
            cardSelectionMode = stage.cardSelectionMode,
            wasIncorrectAnswer = false
        )
    }

    /**
     * Loads next stage into changeableData.
     * Returns false when lesson is finished and settings.isLoop == false.
     */
    private suspend fun advanceToNextStageOrNull(): Boolean {
        if (nextStageIndex !in parameters.stages.indices) {
            if (parameters.settings.isLoop) nextStageIndex = 0 else return false
        }

        currentStageIndex = nextStageIndex
        val stage = parameters.stages[currentStageIndex]

        currentStageCardList = getCards(currentStageIndex)

        changeableData = LessonStageData(
            type = stage.type,
            cards = currentStageCardList.toMutableList(),
            cycles = stage.cycles,
            priority = stage.priority,
            cardSelectionMode = stage.cardSelectionMode,
            wasIncorrectAnswer = false
        )

        nextStageIndex++
        return true
    }
}

/* -------------------- Validation protocol -------------------- */

sealed class LessonValidationCommand {
    data class NextValidation(val nextValidation: LessonValidation) : LessonValidationCommand()
    data object NextCard : LessonValidationCommand()
    data object NextCycle : LessonValidationCommand()
    data object NextStage : LessonValidationCommand()
    data object RestartStage : LessonValidationCommand()
    data object Finished : LessonValidationCommand()
}

interface LessonValidation {
    operator fun invoke(lesson: Lesson): LessonValidationCommand
}

class LessonValidationIsHaveCards : LessonValidation {
    override fun invoke(lesson: Lesson): LessonValidationCommand =
        if (lesson.changeableData.cards.isNotEmpty()) LessonValidationCommand.NextCard
        else LessonValidationCommand.NextValidation(LessonValidationWasIncorrectAnswer())
}

class LessonValidationWasIncorrectAnswer : LessonValidation {
    override fun invoke(lesson: Lesson): LessonValidationCommand =
        if (lesson.changeableData.wasIncorrectAnswer) LessonValidationCommand.RestartStage
        else LessonValidationCommand.NextValidation(LessonValidationAreCyclesLeft())
}

class LessonValidationAreCyclesLeft : LessonValidation {
    override fun invoke(lesson: Lesson): LessonValidationCommand =
        if (lesson.changeableData.cycles > 1) LessonValidationCommand.NextCycle
        else LessonValidationCommand.NextValidation(LessonValidationCanAdvanceStage())
}

class LessonValidationCanAdvanceStage : LessonValidation {
    override fun invoke(lesson: Lesson): LessonValidationCommand {
        val hasNextStage = lesson.parameters.settings.isLoop ||
                lesson.nextStageIndex in lesson.parameters.stages.indices

        return if (hasNextStage) LessonValidationCommand.NextStage
        else LessonValidationCommand.Finished
    }
}


/* -------------------- Models -------------------- */

data class LessonParameters(
    val deckNames: List<String>,
    val stages: List<LessonStage>,
    val settings: LessonSettings
)

data class LessonStage(
    val type: StageType,
    val cards: Int,
    val cycles: Int,
    val priority: Int = 0,
    val cardSelectionMode: CardSelectionMode = CardSelectionMode.Random
)

data class LessonStageData(
    val type: StageType,
    val cards: MutableList<Card>,
    var cycles: Int,
    val priority: Int,
    val cardSelectionMode: CardSelectionMode,
    var wasIncorrectAnswer: Boolean = false,
)

enum class StageType { LEARNING, REPEATING }

data class LessonSettings(
    val isLoop: Boolean = false
)

sealed class CardSelectionMode(val label: String) {
    object Random : CardSelectionMode("Random")
    object PreferLowPriority : CardSelectionMode("PreferLowPriority")
    object PreferHighPriority : CardSelectionMode("PreferHighPriority")
    object LockToPriority : CardSelectionMode("LockToPriority")
}

/**
 * ui == null => lesson finished (or no next card); check notification.
 * notification == null => nothing to show.
 */
data class LessonStepResult(
    val ui: LessonLearningUiState? = null,
    val context: LessonStageData,
    val notification: AppNotification? = null
)
