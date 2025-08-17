package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.constants.MAX_DECK_NAME_LENGTH
import com.example.echolex.core.constants.MIN_DECK_NAME_LENGTH
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.useCase.validation.BaseNameValidationBuilderUseCase
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextValidationRule
import javax.inject.Inject

class ValidateDeckNameUseCase @Inject constructor(
    private val validationBuilder: BaseNameValidationBuilderUseCase
){
    operator fun invoke(name: String): AppNotification {
        val validationRules: List<TextValidationRule> = validationBuilder(
            fieldName = "Deck",
            min = MIN_DECK_NAME_LENGTH,
            max = MAX_DECK_NAME_LENGTH
        )

        for (rule in validationRules) {
            val result = rule.validate(name)
            if (result != AppNotification.Null) {
                return result
            }
        }

        return AppNotification.Null
    }
}