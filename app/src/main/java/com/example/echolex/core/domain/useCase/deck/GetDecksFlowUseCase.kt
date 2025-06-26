package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.repository.DeckMemoryStore
import javax.inject.Inject

class GetDecksFlowUseCase @Inject constructor(private val deckMemoryStore: DeckMemoryStore) {
    operator fun invoke() = deckMemoryStore.decks
}