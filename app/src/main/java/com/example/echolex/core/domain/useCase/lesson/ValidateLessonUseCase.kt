package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class ValidateLessonUseCase @Inject constructor(
    private val validateNameLessonUseCase: ValidateNameLessonUseCase,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
){
    operator fun invoke(lesson: Lesson): Boolean {
        if(!validateNameLessonUseCase(lesson.name)) {
            return false
        }
        if(!validateIndexCount(lesson)) {
            return false
        }

        return true
    }

    private fun validateIndexCount(lesson: Lesson): Boolean {
        if(lesson.parameters.deckNames.isEmpty()) {
            openAppNotificationUseCase(AppNotification.Business.LessonDecksAreEmpty)
            return false
        }
        if(lesson.parameters.stages.isEmpty()) {
            openAppNotificationUseCase(AppNotification.Business.BlueprintsStagesAreEmpty)
            return false
        }
        return true
    }
}