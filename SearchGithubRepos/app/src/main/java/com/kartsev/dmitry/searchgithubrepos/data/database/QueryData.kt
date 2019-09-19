package com.kartsev.dmitry.searchgithubrepos.data.database

import androidx.room.Entity
import com.kartsev.dmitry.searchgithubrepos.data.database.QueryData.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME, primaryKeys = ["query"])
data class QueryData(
    val query: String,
    val repoIds: List<Int>,
    val totalCount: Int,
    val nextPage: Int
) {
    companion object {
        const val TABLE_NAME = "queries"
    }
}