package com.kartsev.dmitry.searchgithubrepos.presentation.search

sealed class SearchUIEventState

data class Failed(val message: String): SearchUIEventState()
data class ShowDetailsAction(val id: Int, val owner: String): SearchUIEventState()