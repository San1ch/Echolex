package com.example.echolex.core.data.local.room.dao

import android.content.Context
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.Transaction
import com.example.echolex.core.data.local.room.AppDatabase
import com.example.echolex.core.domain.data.repository.CardEntity
import com.example.echolex.core.domain.data.repository.DeckEntity
import com.example.echolex.core.domain.data.repository.DeckWithCards
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Provides
    @Singleton
    fun provideAppScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
}


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDb(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "echolex.db")
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideDeckDao(db: AppDatabase): DeckDao = db.deckDao()
}


@Dao
interface DeckDao {

    // ---- Observe ----
    @Transaction
    @Query("SELECT * FROM decks ORDER BY name ASC")
    fun observeDecks(): Flow<List<DeckWithCards>>

    // ---- Get ----
    @Transaction
    @Query("SELECT * FROM decks WHERE name = :name LIMIT 1")
    fun getDeckByNameBlocking(name: String): DeckWithCards?

    // ---- Insert/Update ----
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDeck(deck: DeckEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCards(cards: List<CardEntity>)

    @Query("DELETE FROM cards WHERE deckName = :deckName")
    suspend fun deleteCardsByDeck(deckName: String)

    // Blocking versions for non-suspend repository methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertDeckBlocking(deck: DeckEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertCardsBlocking(cards: List<CardEntity>)

    @Query("DELETE FROM cards WHERE deckName = :deckName")
    fun deleteCardsByDeckBlocking(deckName: String)

    // ---- Delete ----
    @Query("DELETE FROM decks WHERE name = :name")
    suspend fun deleteDeckByName(name: String)

    @Query("DELETE FROM decks WHERE name = :name")
    fun deleteDeckByNameBlocking(name: String)

    // ---- Replace all ----
    @Query("DELETE FROM decks")
    fun clearAllDecksBlocking()

    @Query("DELETE FROM decks")
    suspend fun clearAllDecks()

    // ---- Rename ----
    @Query("UPDATE decks SET name = :newName WHERE name = :oldName")
    fun renameDeckBlocking(oldName: String, newName: String)

    @Query("UPDATE cards SET deckName = :newName WHERE deckName = :oldName")
    fun renameCardsDeckBlocking(oldName: String, newName: String)

    // ---- Card updates ----
    @Query("""
        UPDATE cards
        SET repeatingCount = repeatingCount + 1
        WHERE deckName = :deckName AND firstWord = :firstWord AND secondWord = :secondWord
    """)
    fun incrementRepeatingBlocking(deckName: String, firstWord: String, secondWord: String)

    @Query("""
        UPDATE cards
        SET isPreLearned = 1
        WHERE deckName = :deckName AND firstWord = :firstWord AND secondWord = :secondWord
    """)
    fun markPreLearnedBlocking(deckName: String, firstWord: String, secondWord: String)
}
