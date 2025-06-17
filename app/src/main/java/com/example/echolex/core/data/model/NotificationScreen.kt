package com.example.echolex.core.data.model

import com.example.echolex.core.constants.MAX_DECK_NAME_LENGTH
import com.example.echolex.core.constants.MIN_DECK_NAME_LENGTH



sealed class AppNotification(
    open val title: String,
    open val message: String
) {
    // ======================
    // 1. Default State
    // ======================
    data object Null : AppNotification("", "") {
        override val title = ""
        override val message = ""
    }

    // ======================
    // 2. Success Messages
    // ======================
    sealed class Success : AppNotification("", "") {
        data object Valid : Success() {
            override val title = "Valid"
            override val message = "Input is acceptable."
        }
    }

    // ======================
    // 3. Validation Errors
    // ======================
    sealed class Validation : AppNotification("", "") {
        data object InvalidLength : Validation() {
            override val title = "Invalid Length"
            override val message = "Name must be between $MIN_DECK_NAME_LENGTH and $MAX_DECK_NAME_LENGTH characters."
        }

        data object InvalidStartChar : Validation() {
            override val title = "Invalid Start Character"
            override val message = "Name must start with a letter."
        }

        data object InvalidCharacters : Validation() {
            override val title = "Invalid Characters"
            override val message = "Name can only contain letters, digits, and spaces."
        }

        data object EmptyCardList : Validation() {
            override val title = "Empty Card List"
            override val message = "No card entries found. Please provide at least one word pair."
        }

        data object InvalidCommaFormat : Validation() {
            override val title = "Invalid Format"
            override val message = "Each entry must contain exactly one comma separating word and translation."
        }

        data object EmptyWordOrTranslation : Validation() {
            override val title = "Empty Word or Translation"
            override val message = "Each entry must have both a word and its translation filled in."
        }

        data object ContainsDigits : Validation() {
            override val title = "Digits Detected"
            override val message = "Words and translations should not contain numbers."
        }

        data object ContainsSpecialSymbols : Validation() {
            override val title = "Special Symbols Detected"
            override val message = "Words and translations should not contain special symbols."
        }
    }

    // ======================
    // 4. Business Logic Errors
    // ======================
    sealed class Business : AppNotification("", "") {
        data object SameDeckName : Business() {
            override val title = "Same Name"
            override val message = "The deck with that name already exists"
        }
        data class SameCards(val cardWord: String) : Business() {
            override val title = "Same Cards"
            override val message = "The deck has same cards. Card: $cardWord"
        }

        data object CardDoesNotExist : Business() {
            override val title = "Error"
            override val message = "Card doesn't exist"
        }

        data object DeckDoesNotExist : Business() {
            override val title = "Error"
            override val message = "Deck doesn't exist"
        }

        data object CardAlreadyExists : Business() {
            override val title = "Error"
            override val message = "Card already exists in this deck"
        }
    }

    sealed class Error : AppNotification("", "") {
        data class Generic(val appMessage: String) : Error() {
            override val title = "Error"
            override val message = appMessage
        }
    }

    fun isValidationError() = this is Validation
    fun isBusinessError() = this is Business
    fun isSuccess() = this is Success
}


data class ScreenNotificationData(
    val labelText: String,
    val notificationText: String,
)