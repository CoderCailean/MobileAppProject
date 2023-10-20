package com.example.project.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val username: String,
    val password: String
)

@Entity(
    indices = [Index("userId")],
    tableName = "decks",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = 1
        )
    ]
)
data class DeckEntity(
    @PrimaryKey(autoGenerate = true)
    val deckId: Int,
    val deckName: String,
    val heroName: String,
    val userId: Int
)

@Entity(
    indices = [Index("deckId")],
    tableName = "deck_cards",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["deckId"],
            childColumns = ["deckId"],
            onDelete = 1
        )
    ]
)
data class DeckCardEntity(
    @PrimaryKey(autoGenerate = true)
    val deckCardId: Int,
    val cardName: String,
    val setId: Int,
    val deckId: Int,
)
