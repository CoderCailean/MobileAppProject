package com.example.project.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/*
 * Provides database accessibility for user entities
 */
@Dao
interface UserDAO {

    // CRUD functionality
    @Insert
    suspend fun addUser(userEntity: UserEntity): Long

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUser(username: String): UserEntity

    @Query("SELECT * FROM users WHERE username = :username")
    fun checkUser(username: String): Int

    @Delete
    suspend fun deleteUser(entity: UserEntity)

    @Update
    suspend fun updateUser(entity: UserEntity)

}

/*
 * Provides database accessibility for deck entities
 */
@Dao
interface DeckDAO {

    // CRUD functionality
    @Insert
    suspend fun addDeck(deckEntity: DeckEntity): Long

    @Query("SELECT * FROM decks WHERE userId = :userId")
    fun getDecks(userId: Int): Flow<List<DeckEntity>>

    @Query("SELECT * FROM decks WHERE userId = :userId AND deckName = :deckName")
    fun getDeck(userId: Int, deckName: String): DeckEntity

    @Delete
    suspend fun deleteDeck(entity: DeckEntity)

    @Update
    suspend fun updateDeck(entity: DeckEntity)

}

/*
 * Provides database accessibility for deck card entities
 */
@Dao
interface DeckCardDAO {

    // CRUD functionality
    @Insert
    suspend fun addDeckCard(deckCardEntity: DeckCardEntity)

    @Query("SELECT * FROM deck_cards")
    fun getDeckCards(): Flow<List<DeckCardEntity>>

    @Delete
    suspend fun deleteDeckCard(entity: DeckCardEntity)

    @Update
    suspend fun updateDeckCard(entity: DeckCardEntity)

}

@Dao
interface CardDAO {

    // CRUD functionality
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCard(cardEntity: CardEntity)

    @Query("SELECT * FROM cards")
    fun getCards(): Flow<List<CardEntity>>

    @Query("SELECT COUNT(cardId) FROM cards")
    fun getRowCount(): Int

    @Delete
    suspend fun deleteCard(entity: CardEntity)

    @Update
    suspend fun updateCard(entity: CardEntity)

}
