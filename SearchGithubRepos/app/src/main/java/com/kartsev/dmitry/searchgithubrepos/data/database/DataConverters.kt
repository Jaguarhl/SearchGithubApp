package com.kartsev.dmitry.searchgithubrepos.data.database

import androidx.room.TypeConverter

object DataConverters {
    @TypeConverter
    @JvmStatic
    fun fromIntList(list: List<Int>): String = list.joinToString(",") { it.toString() }

    @TypeConverter
    @JvmStatic
    fun toIntList(string: String): List<Int> = string.split(",").map { it.toInt() }
}