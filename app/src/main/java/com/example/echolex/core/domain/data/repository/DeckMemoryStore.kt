package com.example.echolex.core.domain.data.repository

import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.service.JSONSaver
import com.example.echolex.core.domain.service.decksFileName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeckMemoryStore @Inject constructor(
    private val deckFileSaver: JSONSaver<List<Deck>> ,
) {
    private val _decks = MutableStateFlow<List<Deck>>(emptyList())
    val decks: StateFlow<List<Deck>> = _decks.asStateFlow()

    private val cardIndexesPerDeck: MutableMap<String, Map<Pair<String, String>, Int>> = mutableMapOf()

    init {
        loadInitialDecks()
        buildCardIndexes(decks.value)
    }

    private fun buildCardIndexes(decks: List<Deck>) {
        cardIndexesPerDeck.clear()
        for (deck in decks) {
            val cardIndexMap = mutableMapOf<Pair<String, String>, Int>()
            for ((i, card) in deck.cards.withIndex()) {
                cardIndexMap[Pair(card.firstWord, card.secondWord)] = i
            }
            cardIndexesPerDeck[deck.name] = cardIndexMap
        }
    }

    private fun findCardRoute(firstWord: String, secondWord: String): Pair<Int, CardIndexResult> {
        val pair = Pair(firstWord, secondWord)
        decks.value.forEachIndexed { index, deck ->
            val cardIndex = cardIndexesPerDeck[deck.name]?.get(pair)
            if (cardIndex != null) {
                return index to CardIndexResult.Success(cardIndex)
            }
        }
        return -1 to CardIndexResult.NotFound
    }

    private inline fun updateCardsInDecks(
        deckNames: List<String>,
        crossinline transform: (Card) -> Card?
    ) {
        _decks.value = _decks.value.map { deck ->
            if (deck.name !in deckNames) return@map deck

            val updatedCards = deck.cards.map { card ->
                transform(card) ?: card
            }

            deck.copy(cards = updatedCards)
        }
    }

    fun incrementCardsRepeatingInDeck(cardsToIncrement: List<Card>, deckNames: List<String>) {
        val targets = cardsToIncrement.map { it.firstWord to it.secondWord }.toSet()

        updateCardsInDecks(deckNames) { card ->
            if ((card.firstWord to card.secondWord) in targets) {
                card.copy(repeatingCount = card.repeatingCount + 1)
            } else null
        }
    }

    fun markCardsAsPreLearned(cardsToMark: List<Card>, deckNames: List<String>) {
        val targets = cardsToMark.map { it.firstWord to it.secondWord }.toSet()

        updateCardsInDecks(deckNames) { card ->
            if ((card.firstWord to card.secondWord) in targets) {
                card.copy(isPreLearned = true)
            } else null
        }
    }

    fun setDecks(decks: List<Deck>) {
        _decks.value = decks
        saveDecks()
    }


    private fun loadInitialDecks() {
        val savedDecks = deckFileSaver.load(decksFileName, listOf())
        _decks.value = savedDecks
    }

    suspend fun addDeck(deck: Deck) {
        _decks.update { currentDecks ->
            val updated = listOf(deck) + currentDecks
            deckFileSaver.save(updated, decksFileName)
            updated
        }
    }

    suspend fun removeDeckByName(name: String) {
        _decks.update { currentDecks ->
            val updated = currentDecks.filterNot { it.name == name }
            deckFileSaver.save(updated, decksFileName)
            updated
        }
    }

    fun getDeckByName(name: String): DeckFindResult {
        val deck = decks.value.find { it.name == name }
        return if (deck != null) DeckFindResult.Success(deck) else DeckFindResult.NotFound
    }

    private fun saveDecks() {
        deckFileSaver.save(decks.value, decksFileName)
    }

    fun deckExists(name: String): Boolean {
        return decks.value.any { it.name == name }
    }

    fun changeDeckName(oldName: String, deckWithNewName: Deck) {
        val updatedList = decks.value.map { deck ->
            if (deck.name == oldName) deckWithNewName else deck
        }
        setDecks(updatedList)
    }

    suspend fun updateDeck(deck: Deck) {
        _decks.update { currentDecks ->
            val updated = currentDecks.map {
                if (it.name == deck.name) deck else it
            }
            deckFileSaver.save(updated, decksFileName)
            updated
        }
    }
}

sealed class CardFindResult {
    data class Success(val card: Card) : CardFindResult()
    object NotFound : CardFindResult()
}

sealed class DeckFindResult {
    data class Success(val deck: Deck) : DeckFindResult()
    object NotFound : DeckFindResult()
}

sealed class CardIndexResult {
    data class Success(val index: Int) : CardIndexResult()
    object NotFound : CardIndexResult()
}