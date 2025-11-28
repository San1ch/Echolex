package com.example.echolex.core.domain.data.repository

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import com.example.echolex.core.data.local.room.AppDatabase
import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.lesson.CardSelectionMode
import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.model.lesson.LessonParameters
import com.example.echolex.core.domain.data.model.lesson.LessonSettings
import com.example.echolex.core.domain.data.model.lesson.LessonStage
import com.example.echolex.core.domain.data.model.lesson.LessonStageData
import com.example.echolex.core.domain.data.model.lesson.StageType
import com.example.echolex.core.domain.useCase.deck.GetCardsByDeckNamesAndParametersUseCase
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.IncrementRepeatedCardsUseCase
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.MarkCardsAsPreLearnedUseCase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// =======================
// Room storage в ОДНОМУ файлі:
//  1) LessonBlueprintRepository (шаблони)
//  2) LessonRepository (конкретні Lesson з прогресом)
// =======================

/* ===================== Hilt module: DAO (+ optional Gson) ===================== */

@Module
@InstallIn(SingletonComponent::class)
object LessonRoomModule {

    // Якщо Gson вже є в іншому модулі — ВИДАЛИ цей метод, щоб не було дубля.
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    // НЕ надаємо AppDatabase тут! (щоб не було DuplicateBindings)
    @Provides
    fun provideLessonBlueprintDao(db: AppDatabase): LessonBlueprintDao = db.lessonBlueprintDao()

    @Provides
    fun provideLessonDao(db: AppDatabase): LessonDao = db.lessonDao()
}

/* ===================== Stable CardSelectionMode persistence ===================== */

private enum class CardSelectionModeType { Random, PreferLowPriority, PreferHighPriority, LockToPriority }

private fun CardSelectionMode.toType(): CardSelectionModeType = when (this) {
    CardSelectionMode.Random -> CardSelectionModeType.Random
    CardSelectionMode.PreferLowPriority -> CardSelectionModeType.PreferLowPriority
    CardSelectionMode.PreferHighPriority -> CardSelectionModeType.PreferHighPriority
    CardSelectionMode.LockToPriority -> CardSelectionModeType.LockToPriority
}

private fun CardSelectionModeType.toDomain(): CardSelectionMode = when (this) {
    CardSelectionModeType.Random -> CardSelectionMode.Random
    CardSelectionModeType.PreferLowPriority -> CardSelectionMode.PreferLowPriority
    CardSelectionModeType.PreferHighPriority -> CardSelectionMode.PreferHighPriority
    CardSelectionModeType.LockToPriority -> CardSelectionMode.LockToPriority
}

/* ===================== Shared payloads ===================== */

private data class LessonStagePayload(
    val type: StageType,
    val cards: Int,
    val cycles: Int,
    val priority: Int,
    val selectionMode: CardSelectionModeType
)

private fun LessonStage.toPayload(): LessonStagePayload = LessonStagePayload(
    type = type,
    cards = cards,
    cycles = cycles,
    priority = priority,
    selectionMode = cardSelectionMode.toType()
)

private fun LessonStagePayload.toDomain(): LessonStage = LessonStage(
    type = type,
    cards = cards,
    cycles = cycles,
    priority = priority,
    cardSelectionMode = selectionMode.toDomain()
)

/* ===================== 1) LESSON BLUEPRINTS ===================== */

@Entity(
    tableName = "lesson_blueprints",
    indices = [Index("updatedAtEpochMs")]
)
data class LessonBlueprintEntity(
    @PrimaryKey val name: String,
    val payloadJson: String,
    val updatedAtEpochMs: Long
)

@Dao
interface LessonBlueprintDao {
    @Query("SELECT * FROM lesson_blueprints ORDER BY name")
    fun observeAll(): Flow<List<LessonBlueprintEntity>>

    @Query("SELECT * FROM lesson_blueprints WHERE name = :name LIMIT 1")
    fun observeByName(name: String): Flow<LessonBlueprintEntity?>

    @Query("SELECT * FROM lesson_blueprints WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): LessonBlueprintEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM lesson_blueprints WHERE name = :name)")
    suspend fun exists(name: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: LessonBlueprintEntity)

    @Query("DELETE FROM lesson_blueprints WHERE name = :name")
    suspend fun deleteByName(name: String)

    @Query("DELETE FROM lesson_blueprints")
    suspend fun clear()

    @Transaction
    suspend fun rename(oldName: String, newName: String, newPayloadJson: String): Boolean {
        val existing = getByName(oldName) ?: return false
        if (exists(newName)) return false
        upsert(LessonBlueprintEntity(newName, newPayloadJson, System.currentTimeMillis()))
        deleteByName(existing.name)
        return true
    }
}

private data class LessonBlueprintPayload(
    val name: String,
    val stages: List<LessonStagePayload>,
    val settings: LessonSettings
)

private fun LessonBlueprint.toPayload(): LessonBlueprintPayload = LessonBlueprintPayload(
    name = name,
    stages = stages.map { it.toPayload() },
    settings = settings
)

private fun LessonBlueprintPayload.toDomain(): LessonBlueprint = LessonBlueprint(
    name = name,
    stages = stages.map { it.toDomain() },
    settings = settings
)

private fun Gson.parseBlueprintOrNull(json: String): LessonBlueprint? =
    runCatching { fromJson(json, LessonBlueprintPayload::class.java).toDomain() }.getOrNull()

@Singleton
class LessonBlueprintRepository @Inject constructor(
    private val dao: LessonBlueprintDao,
    private val gson: Gson
) {
    val blueprints: Flow<List<LessonBlueprint>> =
        dao.observeAll()
            .map { entities ->
                entities.mapNotNull { e ->
                    e.payloadJson.takeIf { it.isNotBlank() }?.let(gson::parseBlueprintOrNull)
                }
            }
            .distinctUntilChanged()

    fun observeByName(name: String): Flow<LessonBlueprint?> =
        dao.observeByName(name)
            .map { e -> e?.payloadJson?.takeIf { it.isNotBlank() }?.let(gson::parseBlueprintOrNull) }
            .distinctUntilChanged()

    suspend fun getByName(name: String): LessonBlueprint? =
        dao.getByName(name)?.payloadJson?.takeIf { it.isNotBlank() }?.let(gson::parseBlueprintOrNull)

    suspend fun exists(name: String): Boolean = dao.exists(name)

    suspend fun upsert(blueprint: LessonBlueprint) {
        dao.upsert(
            LessonBlueprintEntity(
                name = blueprint.name,
                payloadJson = gson.toJson(blueprint.toPayload()),
                updatedAtEpochMs = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteByName(name: String) = dao.deleteByName(name)

    suspend fun rename(oldName: String, newName: String): Boolean {
        val existing = getByName(oldName) ?: return false
        val renamed = existing.copy(name = newName)
        return dao.rename(oldName, newName, gson.toJson(renamed.toPayload()))
    }

    suspend fun clear() = dao.clear()
}

/* ===================== 2) LESSONS (progress) ===================== */

@Entity(
    tableName = "lessons",
    indices = [Index("updatedAtEpochMs")]
)
data class LessonEntity(
    @PrimaryKey val name: String,
    val payloadJson: String,
    val updatedAtEpochMs: Long
)

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons ORDER BY updatedAtEpochMs DESC")
    fun observeAll(): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE name = :name LIMIT 1")
    fun observeByName(name: String): Flow<LessonEntity?>

    @Query("SELECT * FROM lessons WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): LessonEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM lessons WHERE name = :name)")
    suspend fun exists(name: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: LessonEntity)

    @Query("DELETE FROM lessons WHERE name = :name")
    suspend fun deleteByName(name: String)

    @Query("DELETE FROM lessons")
    suspend fun clear()

    @Transaction
    suspend fun rename(oldName: String, newName: String, newPayloadJson: String): Boolean {
        val existing = getByName(oldName) ?: return false
        if (exists(newName)) return false
        upsert(LessonEntity(newName, newPayloadJson, System.currentTimeMillis()))
        deleteByName(existing.name)
        return true
    }
}

private data class LessonParametersPayload(
    val deckNames: List<String>,
    val stages: List<LessonStagePayload>,
    val settings: LessonSettings
)

private fun LessonParameters.toPayload(): LessonParametersPayload = LessonParametersPayload(
    deckNames = deckNames,
    stages = stages.map { it.toPayload() },
    settings = settings
)

private fun LessonParametersPayload.toDomain(): LessonParameters = LessonParameters(
    deckNames = deckNames,
    stages = stages.map { it.toDomain() },
    settings = settings
)

private data class LessonStageDataPayload(
    val type: StageType,
    val cards: List<Card>,
    val cycles: Int,
    val priority: Int,
    val selectionMode: CardSelectionModeType,
    val wasIncorrectAnswer: Boolean
)

private fun LessonStageData.toPayload(): LessonStageDataPayload = LessonStageDataPayload(
    type = type,
    cards = cards.toList(),
    cycles = cycles,
    priority = priority,
    selectionMode = cardSelectionMode.toType(),
    wasIncorrectAnswer = wasIncorrectAnswer
)

private fun LessonStageDataPayload.toDomain(): LessonStageData = LessonStageData(
    type = type,
    cards = cards.toMutableList(),
    cycles = cycles,
    priority = priority,
    cardSelectionMode = selectionMode.toDomain(),
    wasIncorrectAnswer = wasIncorrectAnswer
)

private data class LessonPayload(
    val name: String,
    val parameters: LessonParametersPayload,
    val nextStageIndex: Int,
    val currentStageIndex: Int,
    val currentStageCardList: List<Card>,
    val changeableData: LessonStageDataPayload
)

private fun Lesson.toPayload(): LessonPayload = LessonPayload(
    name = name,
    parameters = parameters.toPayload(),
    nextStageIndex = nextStageIndex,
    currentStageIndex = currentStageIndex,
    currentStageCardList = currentStageCardList,
    changeableData = changeableData.toPayload()
)

// ✅ ОЦЕ — КЛЮЧОВИЙ ФІКС: додаємо 2 usecase, щоб Lesson зібрався після JSON.
private fun LessonPayload.toLesson(
    getNextCards: GetCardsByDeckNamesAndParametersUseCase,
    incrementRepeatedCardsUseCase: IncrementRepeatedCardsUseCase,
    markCardsAsPreLearnedUseCase: MarkCardsAsPreLearnedUseCase
): Lesson {
    val lesson = Lesson(
        name = name,
        parameters = parameters.toDomain(),
        changeableData = changeableData.toDomain(),
        getNextCards = getNextCards,
        incrementRepeatedCardsUseCase = incrementRepeatedCardsUseCase,
        markCardsAsPreLearnedUseCase = markCardsAsPreLearnedUseCase
    )
    lesson.nextStageIndex = nextStageIndex
    lesson.currentStageIndex = currentStageIndex
    lesson.currentStageCardList = currentStageCardList
    return lesson
}

private fun Gson.parseLessonOrNull(
    json: String,
    getNextCards: GetCardsByDeckNamesAndParametersUseCase,
    incrementRepeatedCardsUseCase: IncrementRepeatedCardsUseCase,
    markCardsAsPreLearnedUseCase: MarkCardsAsPreLearnedUseCase
): Lesson? = runCatching {
    fromJson(json, LessonPayload::class.java)
        .toLesson(getNextCards, incrementRepeatedCardsUseCase, markCardsAsPreLearnedUseCase)
}.getOrNull()

@Singleton
class LessonRepository @Inject constructor(
    private val dao: LessonDao,
    private val gson: Gson,
    private val getNextCards: GetCardsByDeckNamesAndParametersUseCase,
    private val incrementRepeatedCardsUseCase: IncrementRepeatedCardsUseCase,
    private val markCardsAsPreLearnedUseCase: MarkCardsAsPreLearnedUseCase
) {

    val lessons: Flow<List<Lesson>> =
        dao.observeAll()
            .map { entities ->
                entities.mapNotNull { e ->
                    e.payloadJson.takeIf { it.isNotBlank() }?.let { json ->
                        gson.parseLessonOrNull(
                            json = json,
                            getNextCards = getNextCards,
                            incrementRepeatedCardsUseCase = incrementRepeatedCardsUseCase,
                            markCardsAsPreLearnedUseCase = markCardsAsPreLearnedUseCase
                        )
                    }
                }
            }
            .distinctUntilChanged()

    fun observeByName(name: String): Flow<Lesson?> =
        dao.observeByName(name)
            .map { e ->
                e?.payloadJson?.takeIf { it.isNotBlank() }?.let { json ->
                    gson.parseLessonOrNull(
                        json = json,
                        getNextCards = getNextCards,
                        incrementRepeatedCardsUseCase = incrementRepeatedCardsUseCase,
                        markCardsAsPreLearnedUseCase = markCardsAsPreLearnedUseCase
                    )
                }
            }
            .distinctUntilChanged()

    suspend fun getByName(name: String): Lesson? =
        dao.getByName(name)?.payloadJson?.takeIf { it.isNotBlank() }?.let { json ->
            gson.parseLessonOrNull(
                json = json,
                getNextCards = getNextCards,
                incrementRepeatedCardsUseCase = incrementRepeatedCardsUseCase,
                markCardsAsPreLearnedUseCase = markCardsAsPreLearnedUseCase
            )
        }

    suspend fun exists(name: String): Boolean = dao.exists(name)

    suspend fun upsert(lesson: Lesson) {
        dao.upsert(
            LessonEntity(
                name = lesson.name,
                payloadJson = gson.toJson(lesson.toPayload()),
                updatedAtEpochMs = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteByName(name: String) = dao.deleteByName(name)

    suspend fun rename(oldName: String, newName: String): Boolean {
        val existing = getByName(oldName) ?: return false
        val newJson = gson.toJson(existing.toPayload().copy(name = newName))
        return dao.rename(oldName, newName, newJson)
    }

    suspend fun clear() = dao.clear()
}