package com.example.project.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

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

@Dao
interface DeckDAO {

    // CRUD functionality
    @Insert
    suspend fun addDeck(deckEntity: DeckEntity)

    @Query("SELECT * FROM decks WHERE userId = :userId")
    fun getDecks(userId: Int): Flow<List<DeckEntity>>

    @Delete
    suspend fun deleteDeck(entity: DeckEntity)

    @Update
    suspend fun updateDeck(entity: DeckEntity)

}

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