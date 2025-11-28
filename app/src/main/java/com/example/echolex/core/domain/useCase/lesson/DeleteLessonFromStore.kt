package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.LessonRepository
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class DeleteLessonFromStore @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val getLessonByNameUseCase: GetLessonByNameUseCase,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {
    suspend operator fun invoke(lesson: Lesson): Boolean {
        val result: Result<Lesson> = getLessonByNameUseCase(lesson.name)

        return result
            .onSuccess { foundLesson ->
                lessonRepository.deleteByName(foundLesson.name)
            }
            .onFailure {
                openAppNotificationUseCase(AppNotification.Business.LessonDoesNotExist)
            }
            .isSuccess
    }
}
