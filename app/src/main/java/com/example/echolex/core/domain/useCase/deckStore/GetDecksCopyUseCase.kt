package com.example.echolex.core.domain.useCase.deckStore

import com.example.echolex.core.data.repository.DeckMemoryStore
import javax.inject.Inject

class GetDecksCopyUseCase @Inject constructor(
    private val deckMemoryStore: DeckMemoryStore
) {
    suspend operator fun invoke() = deckMemoryStore.decks.value.toList()
}