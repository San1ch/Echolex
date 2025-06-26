package com.example.echolex.core.domain.validation.newValdations

import com.example.echolex.core.domain.data.model.notification.AppNotification

class ValidationChain<T>(
    private val rules: List<ValidationRule<T>>
) {
    fun validate(input: T): AppNotification {
        for (rule in rules) {
            val result = rule.validate(input)
            if (result != AppNotification.Null) return result
        }
        return AppNotification.Null
    }
}
