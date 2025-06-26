package com.example.echolex.core.domain.useCase.screensUseCases

import com.example.echolex.core.domain.service.centralScreenService.NavigationCenter
import javax.inject.Inject

class BackToPreviousScreenUseCase @Inject constructor(
    private val navigationCenter: NavigationCenter
) {
    operator fun invoke() {
        navigationCenter.backToPreviousScreen()
    }
}