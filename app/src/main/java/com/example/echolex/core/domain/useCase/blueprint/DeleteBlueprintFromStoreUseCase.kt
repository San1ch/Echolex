package com.example.echolex.core.domain.useCase.blueprint

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.LessonBlueprintRepository
import com.example.echolex.core.domain.data.repository.LessonRepository
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class DeleteBlueprintFromStoreUseCase @Inject constructor(
    private val lessonBlueprintRepository: LessonBlueprintRepository,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {

    suspend operator fun invoke(blueprintName: String) {
        val blueprint = lessonBlueprintRepository.getByName(blueprintName)
        if (blueprint == null) {
            openAppNotificationUseCase(AppNotification.Business.BlueprintDoesNotExist)
            return
        }
        lessonBlueprintRepository.deleteByName(blueprintName)
        openAppNotificationUseCase(AppNotification.Null)
    }
}