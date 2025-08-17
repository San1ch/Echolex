package com.example.echolex.core.domain.data.model.lesson

import com.example.echolex.core.constants.STANDARD_COUNT_OF_CARDS_FOR_LEARNING
import com.example.echolex.core.constants.STANDARD_COUNT_OF_CARDS_FOR_REPEATING
import com.example.echolex.core.constants.STANDARD_COUNT_OF_CORRECT_CYCLES_FOR_LEARNING
import com.example.echolex.core.constants.STANDARD_COUNT_OF_CORRECT_CYCLES_FOR_REPEATING
import com.example.echolex.core.constants.STANDARD_COUNT_REPEATING_PRIORITY
import kotlinx.serialization.Serializable

@Serializable
data class LessonBlueprint(
    val name: String,
    val stages: List<LessonStage>,
    val settings: LessonSettings
) {
    companion object {
        fun createDefaultLearningBlueprint(name: String): LessonBlueprint {
            return LessonBlueprint(
                name = name,
                stages = listOf(
                    LessonStage(
                        type = StageType.LEARNING,
                        cards = STANDARD_COUNT_OF_CARDS_FOR_LEARNING,
                        cycles = STANDARD_COUNT_OF_CORRECT_CYCLES_FOR_LEARNING
                    )
                ),
                settings = LessonSettings()
            )
        }
        
        fun createDefaultRepeatingBlueprint(name: String): LessonBlueprint {
            return LessonBlueprint(
                name = name,
                stages = listOf(
                    LessonStage(
                        type = StageType.REPEATING,
                        cards = STANDARD_COUNT_OF_CARDS_FOR_REPEATING,
                        cycles = STANDARD_COUNT_OF_CORRECT_CYCLES_FOR_REPEATING,
                        priority = STANDARD_COUNT_REPEATING_PRIORITY
                    )
                ),
                settings = LessonSettings()
            )
        }
    }
}