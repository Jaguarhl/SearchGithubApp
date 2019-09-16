package com.kartsev.dmitry.searchgithubrepos.presentation.search

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData
import com.kartsev.dmitry.searchgithubrepos.domain.RepoRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import javax.inject.Inject

class SearchRepoViewModel @Inject constructor(
    private val repoRepository: RepoRepository
): ViewModel(), LifecycleObserver {
    val searchQuery = MutableLiveData<String>()

    val searchResultLiveData = MutableLiveData<RepoData>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        onError(exception)
    }

    init {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun searchRepo(query: String) {
        viewModelScope.async(coroutineExceptionHandler) {
            repoRepository.searchRepoByString(searchQuery.value.toString())
        }
    }
}