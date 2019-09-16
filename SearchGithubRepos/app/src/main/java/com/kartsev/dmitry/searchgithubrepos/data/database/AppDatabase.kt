package com.kartsev.dmitry.searchgithubrepos.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kartsev.dmitry.searchgithubrepos.common.Config.DATABASE_VERSION

@Database(
    entities = [
        RepoData::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}