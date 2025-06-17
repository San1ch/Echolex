package com.example.echolex.core.domain.service

import android.content.Context
import com.example.echolex.core.data.model.dataclass.Deck
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.io.File
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext

class DeckJSONSaver @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val fileName = "decks.json"

    fun saveDecks(decks: List<Deck>) {
        try {
            val jsonString = Json.encodeToString(decks)
            val file = File(context.filesDir, fileName)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadDecks(): List<Deck> {
        return try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                val jsonString = file.readText()
                Json.decodeFromString<List<Deck>>(jsonString)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}