package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.repository.DeckRepository
import javax.inject.Inject

class GetDecksCopyUseCase @Inject constructor(
    private val deckRepository: DeckRepository
) {
    suspend operator fun invoke() = deckRepository.decks.value.toList()
}