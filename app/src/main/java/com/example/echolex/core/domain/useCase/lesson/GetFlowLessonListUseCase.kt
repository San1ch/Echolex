package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.repository.LessonMemoryStore
import javax.inject.Inject

class GetFlowLessonListUseCase @Inject constructor(
    private val lessonMemoryStore: LessonMemoryStore
) {
    operator fun invoke() = lessonMemoryStore.lessons
}