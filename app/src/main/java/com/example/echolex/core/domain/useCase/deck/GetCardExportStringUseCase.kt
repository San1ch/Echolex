package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.deck.Card
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCardExportStringUseCase @Inject constructor(
    private val getDeckByNameUseCase: GetDeckByNameUseCase
){
    suspend operator fun invoke(deckName: String): String{
        val deck = getDeckByNameUseCase(deckName)
        return zipCardsToString(deck.first().cards)
    }

    private fun zipCardsToString(cards: List<Card>): String{
        var exportString = ""
        cards.map {
            exportString += it.firstWord + ", " + it.secondWord + ";" + "\n"
        }
        return exportString
    }
}