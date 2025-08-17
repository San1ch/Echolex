package com.example.echolex.core.domain.useCase.validation

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextLengthInRangeRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextMatchesRegexRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextNotEmptyRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextStartsWithLetterRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextValidationRule
import javax.inject.Inject

class BaseNameValidationBuilderUseCase @Inject constructor(){
    operator fun invoke(fieldName: String, min: Int, max: Int): List<TextValidationRule> {
        return listOf(
            TextNotEmptyRule(
                error = AppNotification.Validation.NameIsEmpty
            ),
            TextStartsWithLetterRule(
                error = AppNotification.Validation.InvalidNameStartChar
            ),
            TextMatchesRegexRule(
                regex = Regex("^[\\p{L}\\p{N} ]+$"),
                error = AppNotification.Validation.InvalidNameCharacters
            ),
            TextLengthInRangeRule(
                min = min, max = max,
                error = AppNotification.Validation.InvalidNameLength(fieldName, min, max)
            ),
        )

    }
}