package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.LessonMemoryStore
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class DeleteLessonFromStore @Inject constructor(
    private val lessonMemoryStore: LessonMemoryStore,
    private val getLessonByNameUseCase: GetLessonByNameUseCase,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {
    operator fun invoke(lesson: Lesson): Boolean {
        val result = getLessonByNameUseCase(lesson.name)
        return when(result) {
            is LessonResult.Success -> {
                lessonMemoryStore.removeLesson(result.lesson)
                true
            }
            is LessonResult.NotFound -> {
                openAppNotificationUseCase(AppNotification.Business.LessonDoesNotExist)
                false
            }
        }
    }
}