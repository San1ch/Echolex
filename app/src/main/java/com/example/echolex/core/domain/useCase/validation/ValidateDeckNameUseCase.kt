package com.example.echolex.core.domain.useCase.validation

import com.example.echolex.core.constants.MAX_DECK_NAME_LENGTH
import com.example.echolex.core.constants.MIN_DECK_NAME_LENGTH
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextLengthInRangeRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextMatchesRegexRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextNotEmptyRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextStartsWithLetterRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextValidationRule
import javax.inject.Inject

class ValidateDeckNameUseCase @Inject constructor(){
    operator fun invoke(name: String): AppNotification {
        val validationRules: List<TextValidationRule> = buildValidations()

        for (rule in validationRules) {
            val result = rule.validate(name)
            if (result != AppNotification.Null) {
                return result
            }
        }

        return AppNotification.Null
    }

    private fun buildValidations(): List<TextValidationRule> {
        return listOf(
            TextNotEmptyRule(AppNotification.Validation.NameIsEmpty),
            TextLengthInRangeRule(
                min = MIN_DECK_NAME_LENGTH,
                max = MAX_DECK_NAME_LENGTH,
                error = AppNotification.Validation.InvalidNameLength(
                    "Deck",
                    MIN_DECK_NAME_LENGTH,
                    MAX_DECK_NAME_LENGTH
                )
            ),
            TextStartsWithLetterRule(
                error = AppNotification.Validation.InvalidNameStartChar
            ),
            TextMatchesRegexRule(
                regex = Regex("^[\\p{L}\\p{N} ]+$"),
                error = AppNotification.Validation.InvalidNameCharacters
            )
        )
    }
}