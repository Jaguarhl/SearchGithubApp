package com.kartsev.dmitry.searchgithubrepos.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData
import com.kartsev.dmitry.searchgithubrepos.domain.RepoRepository
import com.kartsev.dmitry.searchgithubrepos.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepoViewModel @Inject constructor(
    private val repoRepository: RepoRepository
) : ViewModel() {
    var lastQuery: String? = null
    var savedLastVisibleItemPosition: Int? = null
    val query = MutableLiveData<String>()
    val searchResultLiveData = MutableLiveData<List<RepoData>>()
    val searchStateLiveData = MutableLiveData<SearchResultState>()
    val searchUIEvents = SingleLiveEvent<SearchUIEventState>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        searchUIEvents.postValue(Failed(exception.toString()))
    }

    companion object {
        const val LOADING_MORE_DELAY: Long = 300
    }

    init {
        savedLastVisibleItemPosition = 0
    }

    fun setQuery(originalQuery: String) {
        if (originalQuery == lastQuery) return

        originalQuery.also {
            lastQuery = it
            query.value = it
        }
        searchRepo()
    }

    private fun searchRepo() {
        if (lastQuery.isNullOrEmpty()) return

        searchStateLiveData.postValue(Running())
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = withContext(Dispatchers.IO) {
                repoRepository.searchRepoByString(lastQuery!!)
            }

            searchResultLiveData.postValue(result)
            searchStateLiveData.postValue(Success())
            savedLastVisibleItemPosition = 0
        }
    }

    fun repositoryItemClicked(repo: RepoData) {
        searchUIEvents.postValue(ShowDetailsAction(repo.id, repo.owner.login))
    }

    fun loadNextPage() {
        if (lastQuery.isNullOrEmpty()) return

        searchStateLiveData.postValue(Running(false))

        viewModelScope.launch(coroutineExceptionHandler) {
            val result = withContext(Dispatchers.IO) {
                repoRepository.requestMore(lastQuery!!)
            }

            if (result.isNotEmpty()) searchResultLiveData.postValue(result)
            delay(LOADING_MORE_DELAY) // prevent blinking during fast Internet
            searchStateLiveData.postValue(Success(false))
        }
    }
}