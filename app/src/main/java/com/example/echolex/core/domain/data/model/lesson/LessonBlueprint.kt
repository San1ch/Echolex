package com.example.echolex.core.domain.data.model.lesson

import com.example.echolex.core.constants.STANDARD_COUNT_OF_CARDS_FOR_LEARNING
import com.example.echolex.core.constants.STANDARD_COUNT_OF_CARDS_FOR_REPEATING
import com.example.echolex.core.constants.STANDARD_COUNT_OF_CORRECT_CYCLES_FOR_LEARNING
import com.example.echolex.core.constants.STANDARD_COUNT_OF_CORRECT_CYCLES_FOR_REPEATING
import com.example.echolex.core.constants.STANDARD_COUNT_REPEATING_PRIORITY
import com.example.echolex.core.domain.data.model.deck.Deck


data class LessonBlueprint(
    val name: String,
    val stages: List<LessonStage>,
    val settings: LessonSettings
)
data class LessonSettings(
    val isLoop: Boolean
)

sealed interface LessonStage

data class LearningStage(
    val cards: Int = STANDARD_COUNT_OF_CARDS_FOR_LEARNING,
    val cycles: Int = STANDARD_COUNT_OF_CORRECT_CYCLES_FOR_LEARNING,
) : LessonStage

data class RepeatingStage(
    val cards: Int = STANDARD_COUNT_OF_CARDS_FOR_REPEATING,
    val cycles: Int = STANDARD_COUNT_OF_CORRECT_CYCLES_FOR_REPEATING,
    val basePriorityRepeatingLevel: Int = STANDARD_COUNT_REPEATING_PRIORITY,
    val cardSelectionMode: CardSelectionMode = CardSelectionMode.PREFER_HIGH_PRIORITY
) : LessonStage


enum class CardSelectionMode {
    RANDOM,
    PREFER_LOW_PRIORITY,
    PREFER_HIGH_PRIORITY,
    LOCK_TO_PRIORITY

}