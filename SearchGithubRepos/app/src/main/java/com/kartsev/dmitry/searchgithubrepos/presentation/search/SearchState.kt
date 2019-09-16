package com.kartsev.dmitry.searchgithubrepos.presentation.search

sealed class SearchState

data class Running(val firstRequest: Boolean = true): SearchState()
data class Success(val firstRequest: Boolean = true): SearchState()
data class Failed(val message: String): SearchState()