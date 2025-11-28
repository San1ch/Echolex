package com.example.echolex.core.domain.useCase.validation

import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class ValidateBlueprintStagesUseCase @Inject constructor(
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {
    operator fun invoke(lessonBlueprint: LessonBlueprint): Boolean {
        if(lessonBlueprint.stages.isEmpty()) {
            openAppNotificationUseCase(AppNotification.Business.BlueprintsStagesAreEmpty)
            return false
        }
        return true
    }
}

class ValidateBlueprintSettingsUseCase @Inject constructor(
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {
    operator fun invoke(lessonBlueprint: LessonBlueprint): Boolean {
        val validationResult = validate(lessonBlueprint)
        if(validationResult != AppNotification.Null) {
            openAppNotificationUseCase(validationResult)
            return false
        }
        return true
    }

    private fun validate(lessonBlueprint: LessonBlueprint): AppNotification {
        val settings = lessonBlueprint.settings
        // TODO: add validation for future settings
        return AppNotification.Null
    }
}