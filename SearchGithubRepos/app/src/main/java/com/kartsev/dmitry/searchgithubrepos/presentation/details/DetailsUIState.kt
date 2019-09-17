package com.kartsev.dmitry.searchgithubrepos.presentation.details

sealed class DetailsUIState

object Running : DetailsUIState()
object Successful : DetailsUIState()
data class Failed(val message: String) : DetailsUIState()