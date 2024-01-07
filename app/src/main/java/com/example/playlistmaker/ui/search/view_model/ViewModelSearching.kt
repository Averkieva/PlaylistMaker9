package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.ErrorType
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.interactors.SearchingInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.view_model.states.StatesOfSearching
import kotlinx.coroutines.launch

class ViewModelSearching(
    private var searchingInteractor: SearchingInteractor,
    private var searchingHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private var results: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()

    private var searchingLiveData = MutableLiveData<StatesOfSearching>()
    private var searchingHistoryList = MutableLiveData<List<Track>>()

    init {
        searchingLiveData.postValue(StatesOfSearching.SearchAndHistory(searchingHistoryInteractor.provideSearchHistory()))
    }

    fun getSearchingLiveData(): LiveData<StatesOfSearching> {
        return searchingLiveData
    }

    fun requestSearch(searchExpression: String) {
        if (searchExpression.isNotEmpty()) {
            searchingLiveData.postValue(StatesOfSearching.Loading)
            viewModelScope.launch {
                try {
                    searchingInteractor.search(searchExpression)
                        .collect {
                            when (it.message) {
                                ErrorType.CONNECTION_ERROR -> searchingLiveData.postValue(
                                    StatesOfSearching.ErrorConnection
                                )

                                ErrorType.SERVER_ERROR -> searchingLiveData.postValue(
                                    StatesOfSearching.ErrorFound
                                )
                                else -> {
                                    results.postValue(it.data)
                                    searchingLiveData.postValue(
                                        if (it.data.isNullOrEmpty())
                                            StatesOfSearching.ErrorFound
                                        else StatesOfSearching.SearchCompleted(it.data)
                                    )
                                }
                            }
                        }
                } catch (error: Error) {
                    searchingLiveData.postValue(StatesOfSearching.ErrorConnection)
                }
            }
        }
    }

    fun add(it: Track) {
        searchingHistoryInteractor.add(it)
    }

    fun clearHistory() {
        searchingHistoryInteractor.clearHistory()
    }

    fun clearSearchingHistoryList() {
        results.value = emptyList()
        searchingHistoryList.value = searchingHistoryInteractor.provideSearchHistory()
        searchingLiveData.value =
            searchingHistoryList.value?.let { StatesOfSearching.SearchAndHistory(it) }
    }
}