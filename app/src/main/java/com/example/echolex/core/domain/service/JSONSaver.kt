package com.example.echolex.core.domain.service

import android.content.Context
import com.example.echolex.core.domain.data.model.deck.Deck
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.KSerializer

class JSONSaver <T> @Inject constructor(
    @ApplicationContext private val context: Context,
    private val serializer: KSerializer<T>
){
    fun save(data: T, fileName: String) {
        try {
            val jsonString = Json.encodeToString(serializer, data)
            val file = File(context.filesDir, fileName)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun load(fileName: String, default: T): T {
        return try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                val jsonString = file.readText()
                Json.decodeFromString(serializer, jsonString)
            } else {
                val jsonString = Json.encodeToString(serializer, default)
                file.writeText(jsonString)
                default
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
    
    fun deleteFile(fileName: String) {
        try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}