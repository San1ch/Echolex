package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.repository.Lesson
import com.example.echolex.core.domain.data.repository.LessonMemoryStore
import javax.inject.Inject

class CreateLessonFromBlueprintUseCase @Inject constructor(
    private val lessonMemoryStore: LessonMemoryStore
) {
    operator fun invoke(blueprint: LessonBlueprint, decks: List<Deck>) {
        val lesson = Lesson(
            name = blueprint.name,
            decks = decks,
            lessonStages = blueprint.stages,
            lessonSettings = blueprint.settings)
        lessonMemoryStore.addLesson(lesson)
    }
}