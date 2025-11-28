package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.model.lesson.CardSelectionMode
import com.example.echolex.core.domain.data.model.lesson.StageType
import com.example.echolex.core.domain.data.repository.DeckFindResult
import com.example.echolex.core.domain.data.repository.DeckRepository
import javax.inject.Inject

class GetCardsByDeckNamesAndParametersUseCase @Inject constructor(private val deckRepository: DeckRepository) {
    suspend operator fun invoke(
        deckNames: List<String>,
        count: Int,
        priority: Int,
        cardSelectionMode: CardSelectionMode,
        stageType: StageType
    ): List<Card> {
        val decks: MutableList<Deck> =
            deckNames.map {
                val result = deckRepository.getDeckByName(it)
                if (result is DeckFindResult.Success) {
                    result.deck
                } else {
                    throw IllegalArgumentException("Deck $it not found")
                }
            }.toMutableList()
        return getNeedCards(
            decks.flatMap { it.cards },
            count,
            priority,
            cardSelectionMode,
            stageType
        )
    }

    private fun getNeedCards(
        cards: List<Card>,
        count: Int,
        priority: Int,
        cardSelectionMode: CardSelectionMode,
        stageType: StageType
    ): List<Card> {

        val candidates: List<Card> = when (stageType) {
            StageType.LEARNING -> {
                cards.filter { !it.isPreLearned }
            }

            StageType.REPEATING -> {
                val preLearnedCards = cards.filter { it.isPreLearned }
                when (cardSelectionMode) {
                    CardSelectionMode.LockToPriority ->
                        preLearnedCards.filter { it.repeatingCount == priority}

                    CardSelectionMode.PreferHighPriority ->
                        preLearnedCards.filter { it.repeatingCount >= priority}

                    CardSelectionMode.PreferLowPriority ->
                        preLearnedCards.filter { it.repeatingCount <= priority}

                    CardSelectionMode.Random ->
                        preLearnedCards
                }
            }
        }

        return candidates
            .shuffled()
            .take(count)
    }
}