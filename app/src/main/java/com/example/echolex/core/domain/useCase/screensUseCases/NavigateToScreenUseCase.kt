package com.example.echolex.core.domain.useCase.screensUseCases

import com.example.echolex.core.domain.service.centralScreenService.NavigationCenter
import com.example.echolex.core.navigation.NavigationTarget
import javax.inject.Inject

class NavigateToScreenUseCase @Inject constructor(
    private val navigationCenter: NavigationCenter
) {

    operator fun invoke(screen: NavigationTarget) {
        navigationCenter.navigate(screen)
    }
}