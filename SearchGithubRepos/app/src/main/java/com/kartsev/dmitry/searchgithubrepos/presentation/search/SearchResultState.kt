package com.kartsev.dmitry.searchgithubrepos.presentation.search

sealed class SearchResultState

data class Running(val firstRequest: Boolean = true): SearchResultState()
data class Success(val firstRequest: Boolean = true): SearchResultState()