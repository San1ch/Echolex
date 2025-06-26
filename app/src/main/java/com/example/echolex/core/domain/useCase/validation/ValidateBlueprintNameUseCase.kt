package com.example.echolex.core.domain.useCase.validation

import com.example.echolex.core.constants.MAX_BLUEPRINT_NAME_LENGTH
import com.example.echolex.core.constants.MIN_BLUEPRINT_NAME_LENGTH
import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import com.example.echolex.core.domain.validation.newValdations.ValidationChain
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextLengthInRangeRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextMatchesRegexRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextNotEmptyRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextStartsWithLetterRule
import com.example.echolex.core.domain.validation.newValdations.textValidation.TextValidationRule
import javax.inject.Inject

class ValidateBlueprintNameUseCase @Inject constructor(
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) {
    operator fun invoke(blueprint: LessonBlueprint): Boolean {
        val nameValidation = buildValidations()
        for (rule in nameValidation) {
            val result = rule.validate(blueprint.name)
            if (result != AppNotification.Null) {
                openAppNotificationUseCase(result)
                return false
            }
        }
        return true
    }

    private fun buildValidations(): List<TextValidationRule> {
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
                min = MIN_BLUEPRINT_NAME_LENGTH, max = MAX_BLUEPRINT_NAME_LENGTH,
                error = AppNotification.Validation.InvalidNameLength(
                    "Blueprint name",
                    MIN_BLUEPRINT_NAME_LENGTH,
                    MAX_BLUEPRINT_NAME_LENGTH
                ),
            ),
        )
    }
}