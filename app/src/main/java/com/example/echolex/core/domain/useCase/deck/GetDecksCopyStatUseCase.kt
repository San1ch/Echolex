package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.constants.COUNT_OF_REPETITION_TO_LEARN
import com.example.echolex.core.domain.data.model.deck.Deck
import javax.inject.Inject

class GetDecksCopyStatUseCase @Inject constructor(
    val getDecksCopyUseCase: GetDecksCopyUseCase
) {
    suspend operator fun invoke(): Result<AllCardsStats> {
        return try {
            val result = AllCardsStats()
            val decks: List<Deck> = getDecksCopyUseCase()

            result.countOfCards = decks.size.toFloat()

            decks.map {
                it.cards.map {
                    if (!it.isPreLearnedCard) {
                        result.countOfNotLearnedCards++
                    } else {
                        if (it.countOfRepeating >= COUNT_OF_REPETITION_TO_LEARN) {
                            result.countOfLearnedCards++
                        } else {
                            result.countOfPreLearnedCards++
                        }
                    }
                }
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class AllCardsStats(
    var countOfCards: Float = 0f,
    var countOfLearnedCards: Float = 0f,
    var countOfPreLearnedCards: Float = 0f,
    var countOfNotLearnedCards: Float = 0f
)