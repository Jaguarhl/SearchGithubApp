package com.kartsev.dmitry.searchgithubrepos.di

import com.kartsev.dmitry.searchgithubrepos.data.database.QueryDao
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoDao
import com.kartsev.dmitry.searchgithubrepos.data.network.GithubApi
import com.kartsev.dmitry.searchgithubrepos.domain.RepoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRepoRepository(repoDao: RepoDao, queryDao: QueryDao, githubApi: GithubApi): RepoRepository =
        RepoRepository(repoDao, queryDao, githubApi)
}