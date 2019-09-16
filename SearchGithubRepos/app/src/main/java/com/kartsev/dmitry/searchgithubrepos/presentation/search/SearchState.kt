package com.kartsev.dmitry.searchgithubrepos.presentation.search

sealed class SearchState

object Running: SearchState()
object Success: SearchState()
data class Failed(val message: String): SearchState()