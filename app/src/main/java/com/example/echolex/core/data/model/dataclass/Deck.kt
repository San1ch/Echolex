package com.example.echolex.core.data.model.dataclass

import com.example.echolex.core.constants.COUNT_OF_REPETITION_TO_LEARN
import kotlinx.serialization.Serializable


@Serializable
data class Deck(
    val name: String,
    val cards: List<Card> = emptyList<Card>()
) {
    fun getCountOfLearningStatus(): DeckCardsLearningStatus {
        var countLearnedCard = 0
        var countPreLearnedCard = 0
        var countNotLearnedCard = 0
        cards.map {
            if (it.isPreLearnedCard == false) {
                countNotLearnedCard++
            } else {
                if (it.countOfRepeating >= COUNT_OF_REPETITION_TO_LEARN) {
                    countLearnedCard++
                } else {
                    countPreLearnedCard++
                }
            }
        }
        return DeckCardsLearningStatus(
            cards.size.toString(),
            countLearnedCard.toString(),
            countPreLearnedCard.toString(),
            countNotLearnedCard.toString()
        )
    }
}

data class DeckCardsLearningStatus(
    val countCard: String,
    val countLearnedCard: String,
    val countPreLearnedCard: String,
    val countNotLearnedCard: String
) {

}