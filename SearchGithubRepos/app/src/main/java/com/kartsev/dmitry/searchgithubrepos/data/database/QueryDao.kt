package com.kartsev.dmitry.searchgithubrepos.data.database

import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.kartsev.dmitry.searchgithubrepos.data.database.QueryData.Companion.TABLE_NAME
import java.util.Collections

@Dao
abstract class QueryDao {
    @Insert(onConflict = REPLACE)
    abstract fun saveQueryResult(list : List<QueryData>): List<Long>

    @Query("SELECT * FROM $TABLE_NAME WHERE `query` LIKE :query;")
    abstract fun getCachedQuery(query : String): QueryData?

    @Query("SELECT * FROM ${RepoData.TABLE_NAME} WHERE id in (:ids);")
    protected abstract fun getById(ids : List<Int>): LiveData<List<RepoData>>

    fun loadOrdered(repoIds: List<Int>): LiveData<List<RepoData>> {
        val order = SparseIntArray()
        repoIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return Transformations.map(getById(repoIds)) { repositories ->
            Collections.sort(repositories) { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            }
            repositories
        }
    }
}