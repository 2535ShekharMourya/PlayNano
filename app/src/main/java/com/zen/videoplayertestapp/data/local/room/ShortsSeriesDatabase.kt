package com.zen.videoplayertestapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShortsEntity::class], version = 1)
abstract class ShortsSeriesDatabase : RoomDatabase() {
    abstract fun shortsSeriesDao(): ShortsSeriesDao

    companion object {
        @Volatile
        private var INSTANCE: ShortsSeriesDatabase? = null

        fun getInstance(context: Context): ShortsSeriesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShortsSeriesDatabase::class.java,
                    "app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}