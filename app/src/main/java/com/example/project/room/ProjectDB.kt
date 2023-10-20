package com.example.project.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, DeckEntity::class, DeckCardEntity::class ], version = 1, exportSchema = false)
abstract class ProjectDB: RoomDatabase() {
    // Companion Object
    companion object{
        private var INSTANCE: ProjectDB? = null

        fun getInstance(context: Context): ProjectDB{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProjectDB::class.java,
                        "project_db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
    // Allows access to our custom defined DAO functions

    abstract fun userDAO(): UserDAO

    abstract fun deckDAO(): DeckDAO

    abstract fun deckCardDAO(): DeckCardDAO
}