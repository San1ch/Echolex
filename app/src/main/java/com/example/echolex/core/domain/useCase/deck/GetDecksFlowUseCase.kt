package com.example.echolex.core.domain.useCase.deck

import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.repository.DeckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetDecksFlowUseCase @Inject constructor(private val deckRepository: DeckRepository) {
    operator fun invoke(): StateFlow<List<Deck>> = deckRepository.decks
}