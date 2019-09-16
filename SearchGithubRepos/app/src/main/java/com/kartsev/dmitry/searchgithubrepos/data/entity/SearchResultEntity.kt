package com.kartsev.dmitry.searchgithubrepos.data.entity

import com.google.gson.annotations.SerializedName
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData

data class SearchResultEntity(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val items: List<RepoData>,
    @SerializedName("total_count")
    val totalCount: Int
)