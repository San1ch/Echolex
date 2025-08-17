package com.example.echolex.core.domain.validation.newValdations.textValidation

import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.validation.newValdations.ValidationRule

typealias TextValidationRule = ValidationRule<String>

class TextNotEmptyRule(private val error: AppNotification) : ValidationRule<String> {
    override fun validate(input: String): AppNotification {
        return if (input.trim().isNotEmpty()) AppNotification.Null else error
    }
}

class TextLengthInRangeRule(
    private val min: Int,
    private val max: Int,
    private val error: AppNotification
) : ValidationRule<String> {
    override fun validate(input: String): AppNotification {
        val length = input.trim().length
        return if (length in min..max) AppNotification.Null else error
    }
}

class TextStartsWithLetterRule(
    private val error: AppNotification
) : ValidationRule<String> {
    override fun validate(input: String): AppNotification {
        val trimmed = input.trim()
        return if (trimmed.isNotEmpty() && trimmed[0].isLetter()) AppNotification.Null else error
    }
}

class TextMatchesRegexRule(
    private val regex: Regex,
    private val error: AppNotification
) : ValidationRule<String> {
    override fun validate(input: String): AppNotification {
        return if (regex.matches(input.trim())) AppNotification.Null else error
    }
}

abstract class RegexContainsRule(
    private val regex: Regex,
    private val error: AppNotification
) : ValidationRule<String> {
    override fun validate(input: String): AppNotification {
        return if (regex.containsMatchIn(input)) error else AppNotification.Null
    }
}

class ContainsDigitsRule :
    RegexContainsRule(Regex("\\d"), AppNotification.Validation.ContainsDigits)

class ContainsSpecialSymbolsRule : RegexContainsRule(
    Regex("[^\\p{L}\\s,;’'ʼ-]+"),
    AppNotification.Validation.ContainsSpecialSymbols
)

class EmptyEntryListValidationRule(
) : ValidationRule<String> {
    override fun validate(input: String): AppNotification {
        val entries = input.split(";").filter { it.isNotBlank() }
        return if (entries.isEmpty()) AppNotification.Validation.EmptyCardList else AppNotification.Null
    }
}

class EntryCommaCountValidationRule(
) : ValidationRule<String> {
    override fun validate(input: String): AppNotification {
        val entries = input.split(";").filter { it.isNotBlank() }
        for (entry in entries) {
            if (entry.count { it == ',' } != 1) {
                return AppNotification.Validation.InvalidCommaFormat
            }
        }
        return AppNotification.Null
    }
}

//////
