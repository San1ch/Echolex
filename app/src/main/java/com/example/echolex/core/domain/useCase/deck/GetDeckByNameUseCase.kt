package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.repository.DeckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDeckByNameUseCase @Inject constructor(
    private val deckRepository: DeckRepository
){
    operator fun invoke(name: String): Flow<Deck> {
        val deck = deckRepository.decks.map { decks -> decks.find { it.name == name } ?: Deck(name = "")  }
        return deck
    }
}