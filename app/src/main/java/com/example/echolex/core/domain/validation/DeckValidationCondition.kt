package com.example.echolex.core.domain.validation

import com.example.echolex.core.constants.MAX_DECK_NAME_LENGTH
import com.example.echolex.core.constants.MIN_DECK_NAME_LENGTH
import com.example.echolex.core.data.model.AppNotification
import com.example.echolex.core.domain.service.DataDeck

class CorrectSymbolsValidationProvider(
    val validations: List<DeckCreatingValidationCondition>
) {

    fun validate(data: DataDeck): AppNotification {
        for (validation in validations) {
            val result = validation.validate(data)
            if (result != AppNotification.Null) return result
        }
        return AppNotification.Null
    }
}

interface DeckCreatingValidationCondition {
    fun validate(data: DataDeck): AppNotification
}

class DeckNameLengthValidation : DeckCreatingValidationCondition {
    override fun validate(data: DataDeck): AppNotification {
        val name = data.name.trim()
        return if (name.length !in MIN_DECK_NAME_LENGTH..MAX_DECK_NAME_LENGTH)
            AppNotification.Validation.InvalidLength
        else AppNotification.Null
    }
}

class DeckNameStartCharValidation : DeckCreatingValidationCondition {
    override fun validate(data: DataDeck): AppNotification {
        val name = data.name.trim()
        return if (name.isEmpty() || !name[0].isLetter())
            AppNotification.Validation.InvalidStartChar
        else AppNotification.Null
    }
}

class DeckNameCharacterValidation : DeckCreatingValidationCondition {
    override fun validate(data: DataDeck): AppNotification {
        val name = data.name.trim()
        val regex = Regex("^[\\p{L}\\p{N} ]+$")
        return if (!regex.matches(name))
            AppNotification.Validation.InvalidCharacters
        else AppNotification.Null
    }
}

class ImportCardStringValidationNotEmpty : DeckCreatingValidationCondition {
    override fun validate(data: DataDeck): AppNotification {
        val entries = data.words.split(";").filter { it.isNotBlank() }
        return if (entries.isEmpty()) AppNotification.Validation.EmptyCardList else AppNotification.Null
    }
}

class ImportCardStringValidationCorrectCommaCount : DeckCreatingValidationCondition {
    override fun validate(data: DataDeck): AppNotification {
        val entries = data.words.split(";").filter { it.isNotBlank() }
        for (entry in entries) {
            if (entry.count { it == ',' } != 1) {
                return AppNotification.Validation.InvalidCommaFormat
            }
        }
        return AppNotification.Null
    }
}

class ImportCardStringValidationNoEmptyWords : DeckCreatingValidationCondition {
    override fun validate(data: DataDeck): AppNotification {
        val entries = data.words.split(";").filter { it.isNotBlank() }
        for (entry in entries) {
            val parts = entry.split(",").map { it.trim() }
            if (parts.size != 2 || parts.any { it.isEmpty() }) {
                return AppNotification.Validation.EmptyWordOrTranslation
            }
        }
        return AppNotification.Null
    }
}

class ImportCardStringValidationNoDigits : DeckCreatingValidationCondition {
    override fun validate(data: DataDeck): AppNotification {
        val regex = Regex("\\d")
        return if (regex.containsMatchIn(data.words)) AppNotification.Validation.ContainsDigits else AppNotification.Null
    }
}

class ImportCardStringValidationNoSpecialSymbols : DeckCreatingValidationCondition {
    override fun validate(data: DataDeck): AppNotification {
        val regex = Regex("[^\\p{L}\\p{N} ]+")
        return if (regex.containsMatchIn(data.words)) AppNotification.Validation.ContainsSpecialSymbols else AppNotification.Null
    }
}
