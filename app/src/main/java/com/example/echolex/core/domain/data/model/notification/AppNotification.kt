package com.example.echolex.core.domain.data.model.notification

sealed interface AppNotification {
    val title: String
    val message: String

    object Null : AppNotification {
        override val title = ""
        override val message = ""
    }

    sealed interface Success : AppNotification {
        object Valid : Success {
            override val title = "Valid"
            override val message = "Input is acceptable."
        }

        object DeckExported : Success {
            override val title = "Deck Exported"
            override val message = "Deck successfully exported."
        }

    }

    sealed interface Error : AppNotification {
        data class Generic(val appMessage: String) : Error {
            override val title = "Error"
            override val message = appMessage
        }
    }

    sealed interface Validation : AppNotification {
        data class InvalidNameLength(val entity: String, val min: Int, val max: Int) : Validation {
            override val title = "Invalid Length"
            override val message = "$entity name must be between $min and $max characters."
        }

        object InvalidNameStartChar : Validation {
            override val title = "Invalid Start Character"
            override val message = "Name must start with a letter."
        }

        object InvalidNameCharacters : Validation {
            override val title = "Invalid Characters"
            override val message = "Name can only contain letters, digits, and spaces."
        }

        object NameIsEmpty : Validation {
            override val title = "Empty"
            override val message = "Name cannot be empty."
        }

        object EmptyCardList : Validation {
            override val title = "Empty Card List"
            override val message = "No card entries found. Please provide at least one word pair."
        }

        object InvalidCommaFormat : Validation {
            override val title = "Invalid Format"
            override val message = "Each entry must contain exactly one word, comma, translation and paragraph."
        }

        object EmptyWordOrTranslation : Validation {
            override val title = "Empty Word or Translation"
            override val message = "Each entry must have both a word and its translation filled in."
        }

        object ContainsDigits : Validation {
            override val title = "Digits Detected"
            override val message = "Words and translations should not contain numbers."
        }

        object ContainsSpecialSymbols : Validation {
            override val title = "Special Symbols Detected"
            override val message = "Words and translations should not contain special symbols except <,> and <;>."
        }
    }

    sealed interface Business : AppNotification {
        object SameDeckName : Business {
            override val title = "Same Name"
            override val message = "The deck with that name already exists."
        }

        object DecksDoNotExist : Business {
            override val title = "Decks Do Not Exist"
            override val message = "No decks found. Please create a deck first."
        }

        data class SameCards(val cardWord: String) : Business {
            override val title = "Same Cards"
            override val message = "The deck has same cards. Card: $cardWord"
        }

        object CardDoesNotExist : Business {
            override val title = "Error"
            override val message = "Card doesn't exist"
        }

        object DeckDoesNotExist : Business {
            override val title = "Error"
            override val message = "Deck doesn't exist"
        }

        object CardAlreadyExists : Business {
            override val title = "Error"
            override val message = "Card already exists in this deck"
        }

        object BlueprintsDoNotExist : Business {
            override val title = "Error"
            override val message = "Blueprints don`t exist. You need to create a blueprint first."
        }

        object BlueprintDoesNotExist : Business {
            override val title = "Error"
            override val message = "Blueprint doesn't exist. You need to create a blueprint first."
        }

        object BlueprintsStagesAreEmpty : Business {
            override val title = "Error"
            override val message = "You need to have at least one stage in the blueprint"
        }

        object LessonDecksAreEmpty : Business {
            override val title = "Error"
            override val message = "You need to have at least one deck in the lesson"
        }

        object LessonBlueprintsAreEmpty : Business {
            override val title = "Error"
            override val message = "You need to have at least one blueprint in the lesson"
        }
        object LessonDoesNotExist : Business {
            override val title = "Error"
            override val message = "Lesson doesn't exist"
        }
    }

    sealed interface Lesson : AppNotification {
        object LessonFinished : Lesson {
            override val title = "Lesson Finished"
            override val message = "Lesson successfully finished."
        }

        object LessonLessonRestart : Lesson {
            override val title = ""
            override val message = "Full lesson restarted."
        }

        object LessonStageRestarted : Lesson {
            override val title = ""
            override val message = "Lesson restarted"
        }

        object LessonNextStage : Lesson {
            override val title = ""
            override val message = "Next stage"
        }

        object LessonNextCycle : Lesson {
            override val title = ""
            override val message = "Next cycle"
        }

        object LessonNextCard : Lesson {
            override val title = ""
            override val message = "Next card"
        }

    }
}
