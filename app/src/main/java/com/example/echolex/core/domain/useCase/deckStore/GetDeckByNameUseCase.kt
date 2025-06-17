package com.example.echolex.core.domain.useCase.deckStore

import com.example.echolex.core.data.model.dataclass.Deck
import com.example.echolex.core.data.repository.DeckMemoryStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDeckByNameUseCase @Inject constructor(
    private val deckMemoryStore: DeckMemoryStore
){
    operator fun invoke(name: String): Flow<Deck> {
        val deck = deckMemoryStore.decks.map { decks -> decks.find { it.name == name } ?: Deck(name = "")  }
        return deck
    }
}