package com.kartsev.dmitry.searchgithubrepos.data.network

import com.kartsev.dmitry.searchgithubrepos.data.entity.SearchResultEntity
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/repositories")
    fun searchForReposAsync(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int? = null
    ): Deferred<Response<SearchResultEntity>>
}