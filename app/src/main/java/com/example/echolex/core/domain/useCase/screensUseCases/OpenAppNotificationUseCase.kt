package com.example.echolex.core.domain.useCase.screensUseCases

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.service.centralScreenService.NotificationCenter
import javax.inject.Inject

class OpenAppNotificationUseCase @Inject constructor(
    private val notificationCenter: NotificationCenter
){
    operator fun invoke(notification: AppNotification) {
        notificationCenter.setNotification(notification)
    }
}