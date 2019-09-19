package com.kartsev.dmitry.searchgithubrepos.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData.Companion.TABLE_NAME

@Dao
interface RepoDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(list: List<RepoData>): List<Long>

    @Query("SELECT * FROM $TABLE_NAME WHERE (name LIKE :query) OR (description LIKE :query) ORDER BY stars DESC, name ASC;")
    suspend fun reposByQuery(query: String): LiveData<List<RepoData>>

    @Query("DELETE FROM $TABLE_NAME;")
    suspend fun clear()

    @Query("SELECT * FROM $TABLE_NAME WHERE (id LIKE :id) AND (owner_login LIKE :owner);")
    suspend fun repoById(id: Int, owner: String): RepoData
}