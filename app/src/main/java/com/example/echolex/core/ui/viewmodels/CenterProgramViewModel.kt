package com.example.echolex.core.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.service.centralScreenService.NavigationCenter
import com.example.echolex.core.domain.service.centralScreenService.NotificationCenter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CenterProgramViewModel @Inject constructor(
    private val notificationCenter: NotificationCenter,
    private val navigationCenter: NavigationCenter
) : ViewModel() {
    val notificationState = notificationCenter.notificationState
    val navigationTarget = navigationCenter.navTarget
    val shouldNavigateBack = navigationCenter.shouldNavigateBack

    fun closeNotification() {
        notificationCenter.closeNotification()
    }

    fun resetNavigationTarget() {
        navigationCenter.resetTarget()
    }

    fun resetBackMode() {
        navigationCenter.resetBackMode()
    }

}