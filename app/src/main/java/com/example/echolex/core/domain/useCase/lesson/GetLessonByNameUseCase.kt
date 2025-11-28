package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.LessonRepository
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class GetLessonByNameUseCase @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {
    suspend operator fun invoke(name: String): Result<Lesson> {
        if (name == "") {
            throw Exception("name is empty")
        }
        val lesson = lessonRepository.getByName(name)
        if (lesson == null) {
            openAppNotificationUseCase(AppNotification.Business.LessonDoesNotExist)
            return Result.failure(Exception("Lesson does not exist"))
        }
        return Result.success(lesson)
    }
}