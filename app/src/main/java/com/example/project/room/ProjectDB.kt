package com.example.project.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
 * Maintains the Singleton pattern by checking to see if a database instance
 * exists, if one exists it is returned, if not one is created.
 */
@Database(entities = [UserEntity::class, DeckEntity::class, DeckCardEntity::class, CardEntity::class], version = 1, exportSchema = false)
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

    /*
     * Allows access to our custom defined DAO functions
     */
    abstract fun userDAO(): UserDAO

    abstract fun deckDAO(): DeckDAO

    abstract fun deckCardDAO(): DeckCardDAO

    abstract fun cardDAO(): CardDAO
}