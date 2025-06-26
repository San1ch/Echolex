package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.repository.DeckMemoryStore
import javax.inject.Inject

class GetDecksCopyUseCase @Inject constructor(
    private val deckMemoryStore: DeckMemoryStore
) {
    suspend operator fun invoke() = deckMemoryStore.decks.value.toList()
}