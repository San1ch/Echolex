package com.example.echolex.core.domain.data.repository

import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.service.DeckJSONSaver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeckMemoryStore @Inject constructor(
    private val deckFileSaver: DeckJSONSaver,
) {
    private val _decks = MutableStateFlow<List<Deck>>(emptyList())
    val decks: StateFlow<List<Deck>> = _decks.asStateFlow()

    fun setDecks(decks: List<Deck>) {
        _decks.value = decks
        saveDecks()
    }

    init {
        loadInitialDecks()
    }

    private fun loadInitialDecks() {
        val savedDecks = deckFileSaver.loadDecks()
        _decks.value = savedDecks
    }

    suspend fun addDeck(deck: Deck) {
        _decks.update { currentDecks ->
            val updated = listOf(deck) + currentDecks
            deckFileSaver.saveDecks(updated)
            updated
        }
    }

    suspend fun removeDeckByName(name: String) {
        _decks.update { currentDecks ->
            val updated = currentDecks.filterNot { it.name == name }
            deckFileSaver.saveDecks(updated)
            updated
        }
    }

    fun getDeckByName(name: String): Deck? {
        return decks.value.find { it.name == name }
    }

    private fun saveDecks() {
        deckFileSaver.saveDecks(decks.value)
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
            deckFileSaver.saveDecks(updated)
            updated
        }
    }
}