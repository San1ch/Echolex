package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.repository.LessonRepository
import javax.inject.Inject

class GetFlowLessonListUseCase @Inject constructor(
    private val lessonRepository: LessonRepository
) {
    operator fun invoke() = lessonRepository.lessons
}