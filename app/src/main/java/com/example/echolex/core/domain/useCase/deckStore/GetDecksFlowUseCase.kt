package com.example.echolex.core.domain.useCase.deckStore

import com.example.echolex.core.data.repository.DeckMemoryStore
import javax.inject.Inject

class GetDecksFlowUseCase @Inject constructor(private val deckMemoryStore: DeckMemoryStore) {
    operator fun invoke() = deckMemoryStore.decks
}