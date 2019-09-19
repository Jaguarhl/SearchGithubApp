package com.kartsev.dmitry.searchgithubrepos.domain

import androidx.lifecycle.LiveData
import com.kartsev.dmitry.searchgithubrepos.data.database.QueryDao
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoDao
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData
import com.kartsev.dmitry.searchgithubrepos.data.network.GithubApi
import com.kartsev.dmitry.searchgithubrepos.domain.base.BaseRepository
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class RepoRepository @Inject constructor(
    private val repoDao: RepoDao,
    private val queryDao: QueryDao,
    private val githubApi: GithubApi
): BaseRepository() {

    private var lastRequestedPage = 1
    private var isRequestInProgress = false

    suspend fun searchRepoByString(
        query: String
    ): LiveData<List<RepoData>> {
        lastRequestedPage = 1

        val cachedData = queryDao.getCachedQuery(query)
        return if (cachedData != null && !cachedData.repoIds.isNullOrEmpty())
        {
            lastRequestedPage = cachedData.nextPage
            queryDao.loadOrdered(cachedData.repoIds)
        }
        else requestAndCacheRepo(query.prepareQueryString())
    }

    private fun String.prepareQueryString(): String {
        val query = "%${replace(' ', '%')}%"
        Timber.d("SearchRepoByString($query) START.")
        return query
    }

    suspend fun requestMore(
        query: String
    ): LiveData<List<RepoData>> {
        return requestAndCacheRepo(query.prepareQueryString())
    }

    private suspend fun requestAndCacheRepo(
        query: String
    ): LiveData<List<RepoData>> {
        val cachedData = repoDao.reposByQuery(query)
        val cachedQuery = queryDao.getCachedQuery(query)
        if (isRequestInProgress) return cachedData

        isRequestInProgress = true
        val result = safeApiCall(
            call = { githubApi.searchForReposAsync(query, lastRequestedPage).await() },
            errorMessage = "Error Fetching Found Repositories Results."
        )

        if (result?.items != null) {
            val cachedRepoIds = cachedQuery?.repoIds ?: listOf()
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