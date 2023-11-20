package com.example.project.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import Printing

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

data class CardEntity(
    @PrimaryKey
    val unique_id: String,
    val abilities_and_effects: List<Any>,
    val ability_and_effect_keywords: List<Any>,
    val blitz_banned: Boolean,
    val blitz_legal: Boolean,
    val blitz_living_legend: Boolean,
    val blitz_suspended: Boolean,
    val card_keywords: List<Any>,
    val cc_banned: Boolean,
    val cc_legal: Boolean,
    val cc_living_legend: Boolean,
    val cc_suspended: Boolean,
    val commoner_banned: Boolean,
    val commoner_legal: Boolean,
    val commoner_suspended: Boolean,
    val cost: String,
    val defense: String,
    val functional_text: String,
    val functional_text_plain: String,
    val granted_keywords: List<Any>,
    val health: String,
    val intelligence: String,
    val interacts_with_keywords: List<Any>,
    val name: String,
    val pitch: String,
    val played_horizontally: Boolean,
    val power: String,
    val printings: List<Printing>,
    val removed_keywords: List<Any>,
    val type_text: String,
    val types: List<String>,
    val upf_banned: Boolean
)
