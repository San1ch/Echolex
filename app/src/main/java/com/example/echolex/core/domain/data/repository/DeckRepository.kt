package com.example.echolex.core.domain.data.repository

import androidx.compose.foundation.MutatePriority
import com.example.echolex.core.data.local.room.dao.DeckDao
import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.model.lesson.CardSelectionMode
import com.example.echolex.core.domain.data.model.lesson.LessonParameters
import com.example.echolex.core.domain.data.model.lesson.StageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeckRepository @Inject constructor(
    private val deckDao: DeckDao,
    private val appScope: CoroutineScope,
) {
    val decks: StateFlow<List<Deck>> =
        deckDao.observeDecks()
            .map { list -> list.map { it.toDomain() } }
            .stateIn(appScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun incrementCardsRepeatingInDeck(cardsToIncrement: List<Card>, deckNames: List<String>) {
        val targets = cardsToIncrement.map { it.firstWord to it.secondWord }.toSet()

        for (deckName in deckNames) {
            for ((first, second) in targets) {
                deckDao.incrementRepeatingBlocking(deckName, first, second)
            }
        }
    }

    fun markCardsAsPreLearned(cardsToMark: List<Card>, deckNames: List<String>) {
        val targets = cardsToMark.map { it.firstWord to it.secondWord }.toSet()

        for (deckName in deckNames) {
            for ((first, second) in targets) {
                deckDao.markPreLearnedBlocking(deckName, first, second)
            }
        }
    }

    fun setDecks(decks: List<Deck>) {
        deckDao.clearAllDecksBlocking()
        for (deck in decks) {
            deckDao.upsertDeckBlocking(deck.toEntity())
            val cards = deck.cards.map { it.toEntity(deck.name) }
            deckDao.upsertCardsBlocking(cards)
        }
    }

    suspend fun addDeck(deck: Deck) {
        deckDao.upsertDeck(deck.toEntity())
        deckDao.upsertCards(deck.cards.map { it.toEntity(deck.name) })
    }

    suspend fun removeDeckByName(name: String) {
        deckDao.deleteDeckByName(name)
    }

    fun getDeckByName(name: String): DeckFindResult {
        val found = deckDao.getDeckByNameBlocking(name)?.toDomain()
        return if (found != null) DeckFindResult.Success(found) else DeckFindResult.NotFound
    }

    fun deckExists(name: String): Boolean {
        return deckDao.getDeckByNameBlocking(name) != null
    }

    fun changeDeckName(oldName: String, deckWithNewName: Deck) {
        val newName = deckWithNewName.name

        deckDao.renameDeckBlocking(oldName, newName)
        deckDao.renameCardsDeckBlocking(oldName, newName)

        deckDao.deleteCardsByDeckBlocking(newName)
        deckDao.upsertCardsBlocking(deckWithNewName.cards.map { it.toEntity(newName) })
    }

    suspend fun updateDeck(deck: Deck) {
        // REPLACE deck + replace cards
        deckDao.upsertDeck(deck.toEntity())
        deckDao.deleteCardsByDeck(deck.name)
        deckDao.upsertCards(deck.cards.map { it.toEntity(deck.name) })
    }
}



sealed class DeckFindResult {
    data class Success(val deck: Deck) : DeckFindResult()
    object NotFound : DeckFindResult()
}
