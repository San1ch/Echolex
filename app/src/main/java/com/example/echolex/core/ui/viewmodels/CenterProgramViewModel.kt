package com.example.echolex.core.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.echolex.core.data.model.AppNotification
import com.example.echolex.core.navigation.NavigationTarget
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

    fun closeNotification() {
        notificationCenter.closeNotification()
    }

    fun setNotification(notification: AppNotification) {
        notificationCenter.setNotification(notification)
    }

    fun setNavigationTarget(target: NavigationTarget) {
        navigationCenter.navigate(target)
    }

    fun resetNavigationTarget() {
        navigationCenter.resetTarget()
    }

}