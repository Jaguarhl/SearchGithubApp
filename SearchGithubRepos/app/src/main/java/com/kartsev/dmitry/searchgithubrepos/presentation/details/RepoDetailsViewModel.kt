package com.kartsev.dmitry.searchgithubrepos.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoDao
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData
import com.kartsev.dmitry.searchgithubrepos.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoDetailsViewModel @Inject constructor(
    private val repoDao: RepoDao
) : ViewModel() {
    private val _repoDetailsLiveData = MutableLiveData<RepoData>()
    val repoDetailsLiveData: LiveData<RepoData> = _repoDetailsLiveData
    val repoDetailsStateLiveData = MutableLiveData<DetailsResultState>()
    val repoDetailsUIEventState = SingleLiveEvent<DetailsUIState>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        repoDetailsUIEventState.postValue(Failed(exception.message.toString()))
    }

    fun initializeByDetails(repoId: Int, owner: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            repoDetailsStateLiveData.postValue(Running)
            val repo = withContext(Dispatchers.IO) {
                repoDao.repoById(repoId, owner)
            }
            _repoDetailsLiveData.postValue(repo)
            repoDetailsStateLiveData.postValue(Successful)
        }
    }
}