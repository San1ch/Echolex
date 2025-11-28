package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.constants.SharedDataLessonItemNameKey
import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.repository.LessonRepository
import com.example.echolex.core.domain.data.repository.SharedDataMemoryStore
import javax.inject.Inject

class GetCurrentLessonUseCase @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val sharedDataMemoryStore: SharedDataMemoryStore
) {
    suspend operator fun invoke(): Result<Lesson> {
        val name = sharedDataMemoryStore.get<String>(SharedDataLessonItemNameKey)
            ?: return Result.failure(IllegalStateException("No current lesson name in SharedDataMemoryStore"))

        val lesson = lessonRepository.getByName(name)
            ?: return Result.failure(NoSuchElementException("Lesson '$name' not found"))

        return Result.success(lesson)
    }
}
