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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SearchRepoViewModel @Inject constructor(
    private val repoRepository: RepoRepository
) : ViewModel(), LifecycleObserver {
    var lastQuery: String? = null
    var savedLastVisibleItemPosition: Int? = null
    val searchResultLiveData = MutableLiveData<List<RepoData>>()
    val searchStateLiveData = MutableLiveData<SearchUIState>()
    lateinit var searchJob: Job

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        searchStateLiveData.postValue(Failed(exception.toString()))
    }

    init {
        savedLastVisibleItemPosition = 0
    }

    fun setQuery(originalQuery: String) {
        if (originalQuery == lastQuery) return
        lastQuery = originalQuery
        searchRepo()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun searchRepo() {
        if (lastQuery.isNullOrEmpty()) return

        searchStateLiveData.postValue(Running())
        searchJob = viewModelScope.launch(coroutineExceptionHandler) {
            val result = withContext(Dispatchers.IO) {
                repoRepository.searchRepoByString(lastQuery!!)
            }

            if (!isActive) return@launch

            searchResultLiveData.postValue(result)
            searchStateLiveData.postValue(Success())
            savedLastVisibleItemPosition = 0
            Timber.d("searchRepo(): We have now ${result.size} items on \"$lastQuery\" query.")
        }
    }

    fun repositoryItemClicked(repo: RepoData) {
        searchStateLiveData.postValue(ShowDetailsAction(repo.id, repo.owner.login))
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount
            && !searchJob.isActive) {
            if (lastQuery.isNullOrEmpty()) return
            savedLastVisibleItemPosition = lastVisibleItemPosition

            searchStateLiveData.postValue(Running(false))

            searchJob = viewModelScope.launch(coroutineExceptionHandler) {
                val result = withContext(Dispatchers.IO) {
                    repoRepository.requestMore(lastQuery!!)
                }

                if (!isActive) return@launch

                if (result.isNotEmpty()) searchResultLiveData.postValue(result)
                searchStateLiveData.postValue(Success(false))
                Timber.d("listScrolled(): We have now ${result.size} items on \"$lastQuery\" query.")
            }
        }
    }

    companion object {
        const val VISIBLE_THRESHOLD = 5
    }
}