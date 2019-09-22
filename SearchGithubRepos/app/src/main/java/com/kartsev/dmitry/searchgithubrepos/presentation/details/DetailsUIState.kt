package com.kartsev.dmitry.searchgithubrepos.presentation.details

sealed class DetailsUIState

data class Failed(val message: String) : DetailsUIState()