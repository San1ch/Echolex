package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.LessonMemoryStore
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class GetLessonByNameUseCase @Inject constructor(
    private val lessonMemoryStore: LessonMemoryStore,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {
    operator fun invoke(name: String): LessonResult {
        if (name == "") {
            throw Exception("name is empty")
        }
        val lesson = lessonMemoryStore.getLessonByName(name)
        if (lesson == null) {
            openAppNotificationUseCase(AppNotification.Business.LessonDoesNotExist)
            return LessonResult.NotFound
        }
        return LessonResult.Success(lesson)
    }
}

sealed class LessonResult {
    data class Success(val lesson: Lesson) : LessonResult()
    object NotFound : LessonResult()
}