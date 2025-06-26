package com.example.echolex.core.domain.service.centralScreenService

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.navigation.NavigationTarget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationCenter @Inject constructor() {
    //notifications
    private val _notificationStatus = MutableStateFlow<AppNotification>(AppNotification.Null)
    val notificationStatus: StateFlow<AppNotification> = _notificationStatus

    //navigation
    private val _shouldNavigateBack = MutableStateFlow(false)
    val shouldNavigateBack: StateFlow<Boolean> = _shouldNavigateBack

    private val _navTarget = MutableStateFlow<NavigationTarget>(NavigationTarget.NullScreen)
    val navTarget: StateFlow<NavigationTarget> = _navTarget

    fun navigate(target: NavigationTarget) {
        _navTarget.value = target
    }

    fun backToPreviousScreen() {
        _shouldNavigateBack.value = true
    }

    fun resetBackMode() {
        _shouldNavigateBack.value = false
    }

    fun resetTarget() {
        _navTarget.value = NavigationTarget.NullScreen
    }
}