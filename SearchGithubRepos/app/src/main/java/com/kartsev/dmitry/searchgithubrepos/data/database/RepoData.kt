package com.kartsev.dmitry.searchgithubrepos.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index("id"),
        Index("owner_login")],
    primaryKeys = ["name", "owner_login"]
)
data class RepoData(
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("full_name")
    val fullName: String,
    @field:SerializedName("description")
    val description: String?,
    @field:SerializedName("owner")
    @field:Embedded(prefix = "owner_")
    val owner: Owner,
    @field:SerializedName("stargazers_count")
    val stars: Int,
    @field:SerializedName("html_url")
    val url: String,
    @field:SerializedName("forks_count")
    val forks: Int,
    @field:SerializedName("language")
    val language: String?
) {
    data class Owner(
        @field:SerializedName("login")
        val login: String,
        @field:SerializedName("url")
        val url: String?,
        @field:SerializedName("avatar_url")
        val avatar: String?
    )

    fun getStarsCount(): String = stars.toString()
    fun getForksCount(): String = forks.toString()

    companion object {
        const val TABLE_NAME = "repositories"
    }
}