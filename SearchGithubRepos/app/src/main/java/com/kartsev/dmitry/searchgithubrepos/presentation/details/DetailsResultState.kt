package com.kartsev.dmitry.searchgithubrepos.presentation.details

sealed class DetailsResultState

object Running : DetailsResultState()
object Successful : DetailsResultState()