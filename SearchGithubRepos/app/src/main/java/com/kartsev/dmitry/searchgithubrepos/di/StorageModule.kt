package com.kartsev.dmitry.searchgithubrepos.di

import android.app.Application
import androidx.room.Room
import com.kartsev.dmitry.searchgithubrepos.common.Config.DATABASE_NAME
import com.kartsev.dmitry.searchgithubrepos.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {
    @Provides
    @Singleton
    fun provideAppDatabase(app: Application) =  Room.databaseBuilder(
        app, AppDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideRepoDao(db: AppDatabase) = db.repoDao()

    @Provides
    @Singleton
    fun provideQueryDao(db: AppDatabase) = db.queryDao()
}