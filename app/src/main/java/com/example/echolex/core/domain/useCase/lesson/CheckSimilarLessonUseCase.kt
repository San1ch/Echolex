package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.repository.LessonRepository
import javax.inject.Inject

class CheckSimilarLessonUseCase @Inject constructor(
    private val lessonRepository: LessonRepository
) {
    suspend operator fun invoke(name: String): Boolean {
        return lessonRepository.exists(name)
    }
}