package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.repository.DeckRepository
import javax.inject.Inject

class DeckExistsByNameUseCase @Inject constructor(
    private val deckRepository: DeckRepository,){
    operator fun invoke(name: String): Boolean {
        return deckRepository.deckExists(name)
    }
}