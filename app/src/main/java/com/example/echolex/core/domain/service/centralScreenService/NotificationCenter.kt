package com.example.echolex.core.domain.service.centralScreenService

import com.example.echolex.core.data.model.AppNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationCenter @Inject constructor(
) {
    private val _notificationState = MutableStateFlow<AppNotification>(AppNotification.Null)
    val notificationState: StateFlow<AppNotification> = _notificationState

    fun setNotification(notification: AppNotification) {
        _notificationState.value = notification
    }

    fun closeNotification() {
        _notificationState.value = AppNotification.Null
    }

}