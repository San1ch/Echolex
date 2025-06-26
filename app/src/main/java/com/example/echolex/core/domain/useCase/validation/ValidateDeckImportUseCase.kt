package com.example.echolex.core.domain.useCase.validation

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.validation.newValdations.textValidation.ContainsDigitsRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.ContainsSpecialSymbolsRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.EmptyEntryListValidationRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.EntryCommaCountValidationRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextNotEmptyRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextValidationRule
import javax.inject.Inject

class ValidateDeckImportUseCase @Inject constructor(
) {
    operator fun invoke(import: String): AppNotification {
        val validationRules: List<TextValidationRule> = buildValidations()

        for (rule in validationRules) {
            val result = rule.validate(import)
            if (result != AppNotification.Null) {
                return result
            }
        }

        return AppNotification.Null
    }

    private fun buildValidations(): List<TextValidationRule> {
        return listOf(
            TextNotEmptyRule(AppNotification.Validation.NameIsEmpty),
            ContainsDigitsRule(),
            ContainsSpecialSymbolsRule(),
            EmptyEntryListValidationRule(),
            EntryCommaCountValidationRule()
        )
    }
}
