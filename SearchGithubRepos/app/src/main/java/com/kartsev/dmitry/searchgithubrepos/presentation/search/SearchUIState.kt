package com.kartsev.dmitry.searchgithubrepos.presentation.search

sealed class SearchUIState

data class Running(val firstRequest: Boolean = true): SearchUIState()
data class Success(val firstRequest: Boolean = true): SearchUIState()
data class Failed(val message: String): SearchUIState()
data class ShowDetailsAction(val id: Int, val owner: String): SearchUIState()