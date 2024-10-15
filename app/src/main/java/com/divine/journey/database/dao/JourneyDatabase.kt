package com.divine.journey.database.dao

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.divine.journey.database.entites.DbPosts

@Database(
    entities = [DbPosts::class],
    version = 1,
    exportSchema = false
)
abstract class JourneyDatabase : RoomDatabase() {

    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "journey.db"

        @Volatile
        private var INSTANCE: JourneyDatabase? = null

        fun getInstance(@NonNull context: Context): JourneyDatabase {
            if (INSTANCE == null) {
                synchronized(LOCK) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            JourneyDatabase::class.java,
                            DATABASE_NAME
                        ).fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    abstract fun getJourneyDao(): JourneyDao

}