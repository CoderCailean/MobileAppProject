package com.example.project.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.project.model.Printing

/*
 * Establishes the user entity data class
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val username: String,
    val password: String
)

/*
 * Establishes the deck entity data class
 */
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

/*
 * Establishes the deck card entity data class
 */
@Entity(
    indices = [Index("deckId"), Index("cardId")],
    tableName = "deck_cards",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["deckId"],
            childColumns = ["deckId"],
            onDelete = 1
        ),
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["cardId"],
            childColumns = ["cardId"],
            onDelete = 1
        )
    ]
)
data class DeckCardEntity(
    @PrimaryKey(autoGenerate = true)
    val deckCardId: Int,
    val cardId: String,
    val deckId: Int,
)

/**
 * Establishes the Card entity data class
 */
@Entity(
    tableName = "cards",
)
data class CardEntity(
    @PrimaryKey
    val cardId: String,
    val cost: String,
    val defense: String,
    val functionalText: String,
    val functionalTextPlain: String,
    val health: String,
    val intelligence: String,
    val name: String,
    val pitch: String,
    val power: String,
    val typeText: String,
    val imageUrl: String?
)

