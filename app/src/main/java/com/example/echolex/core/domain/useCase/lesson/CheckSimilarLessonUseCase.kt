package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.repository.LessonMemoryStore
import javax.inject.Inject

class CheckSimilarLessonUseCase @Inject constructor(
    private val lessonMemoryStore: LessonMemoryStore
) {
    operator fun invoke(name: String): Boolean {
        return lessonMemoryStore.getLessonByName(name) != null
    }
}