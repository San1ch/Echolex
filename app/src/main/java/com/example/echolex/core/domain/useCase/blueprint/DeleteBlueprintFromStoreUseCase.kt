package com.example.echolex.core.domain.useCase.blueprint

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.LessonBlueprintMemoryStore
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import javax.inject.Inject

class DeleteBlueprintFromStoreUseCase @Inject constructor(
    private val blueprintStore: LessonBlueprintMemoryStore,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {

    operator fun invoke(blueprintName: String) {
        val blueprint = blueprintStore.getBlueprintByName(blueprintName)
        if (blueprint == null) {
            openAppNotificationUseCase(AppNotification.Business.BlueprintDoesNotExist)
            return
        }
        blueprintStore.removeBlueprintByName(blueprintName)
        openAppNotificationUseCase(AppNotification.Null)
    }
}