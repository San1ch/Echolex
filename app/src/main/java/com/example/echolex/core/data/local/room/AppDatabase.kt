package com.example.echolex.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.echolex.core.data.local.room.dao.DeckDao
import com.example.echolex.core.domain.data.repository.CardEntity
import com.example.echolex.core.domain.data.repository.DeckEntity
import com.example.echolex.core.domain.data.repository.LessonBlueprintDao
import com.example.echolex.core.domain.data.repository.LessonBlueprintEntity
import com.example.echolex.core.domain.data.repository.LessonDao
import com.example.echolex.core.domain.data.repository.LessonEntity

@Database(
    entities = [
        DeckEntity::class, CardEntity::class,
        LessonEntity::class, LessonBlueprintEntity::class
    ],
    version = 4, // ⬅️ треба підняти версію, бо додав таблиці
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun lessonDao(): LessonDao
    abstract fun lessonBlueprintDao(): LessonBlueprintDao
}