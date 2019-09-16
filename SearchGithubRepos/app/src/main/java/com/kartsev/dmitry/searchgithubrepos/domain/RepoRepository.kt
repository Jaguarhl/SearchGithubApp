package com.kartsev.dmitry.searchgithubrepos.domain

import com.kartsev.dmitry.searchgithubrepos.data.database.RepoDao
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData
import com.kartsev.dmitry.searchgithubrepos.data.network.GithubApi
import com.kartsev.dmitry.searchgithubrepos.domain.base.BaseRepository
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class RepoRepository @Inject constructor(
    private val repoDao: RepoDao,
    private val githubApi: GithubApi
): BaseRepository() {

    private var lastRequestedPage = 1
    private var isRequestInProgress = false

    suspend fun searchRepoByString(
        query: String
    ): List<RepoData> {
        lastRequestedPage = 1

        return requestAndCacheRepo(query.prepareQueryString())
    }

    private fun String.prepareQueryString(): String {
        val query = "%${replace(' ', '%')}%"
        Timber.d("SearchRepoByString($query) START.")
        return query
    }

    suspend fun requestMore(
        query: String
    ): List<RepoData> {
        return requestAndCacheRepo(query.prepareQueryString())
    }

    private suspend fun requestAndCacheRepo(
        query: String
    ): List<RepoData> {
        val cachedData = repoDao.reposByQuery(query)
        if (isRequestInProgress) return cachedData

        isRequestInProgress = true
        val result = safeApiCall(
            call = { githubApi.searchForReposAsync(query, lastRequestedPage).await() },
            errorMessage = "Error Fetching Found Repositories Results."
        )

        if (result?.items != null) {
            lastRequestedPage++
            try {
                repoDao.save(result.items)
                Timber.d("Insert to database SUCCESS!\n${result.items}")
            } catch (exception: Exception) {
                Timber.w("Insert to database FAILED!", exception)
            }
        }
        isRequestInProgress = false

        return repoDao.reposByQuery(query)
    }
}