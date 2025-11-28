package com.example.echolex.core.domain.useCase.blueprint

import com.example.echolex.core.constants.MAX_BLUEPRINT_NAME_LENGTH
import com.example.echolex.core.constants.MIN_BLUEPRINT_NAME_LENGTH
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import com.example.echolex.core.domain.useCase.validation.BaseNameValidationBuilderUseCase
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextValidationRule
import javax.inject.Inject

class ValidateBlueprintNameUseCase @Inject constructor(
    private val openAppNotificationUseCase: OpenAppNotificationUseCase,
    private val validationBuilder: BaseNameValidationBuilderUseCase
){
    operator fun invoke(name: String): Boolean {
        val validationRules: List<TextValidationRule> = validationBuilder(
            fieldName = "Blueprint",
            min = MIN_BLUEPRINT_NAME_LENGTH,
            max = MAX_BLUEPRINT_NAME_LENGTH
        )
        for (rule in validationRules) {
            val result = rule.validate(name)
            if (result != AppNotification.Null) {
                openAppNotificationUseCase(result)
                return false
            }
        }
        return true
    }
}