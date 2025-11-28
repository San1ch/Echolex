package com.example.echolex.core.domain.data.repository

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "decks"
)
data class DeckEntity(
    @PrimaryKey val name: String
)

@Entity(
    tableName = "cards",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["name"],
            childColumns = ["deckName"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("deckName"),
        Index(value = ["deckName", "firstWord", "secondWord"], unique = true)
    ]
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deckName: String,

    val firstWord: String,
    val secondWord: String,

    val repeatingCount: Int,
    val isPreLearned: Boolean
)


data class DeckWithCards(
    @Embedded val deck: DeckEntity,
    @Relation(
        parentColumn = "name",
        entityColumn = "deckName"
    )
    val cards: List<CardEntity>
)