package com.kartsev.dmitry.searchgithubrepos.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kartsev.dmitry.searchgithubrepos.common.Config.DATABASE_VERSION

@Database(
    entities = [
        RepoData::class, QueryData::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)

@TypeConverters(DataConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
    abstract fun queryDao(): QueryDao
}