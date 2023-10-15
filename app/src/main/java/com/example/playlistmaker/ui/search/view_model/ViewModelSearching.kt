package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.ErrorType
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.interactors.SearchingInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.view_model.states.StatesOfSearching

class ViewModelSearching(
    private var searchingInteractor: SearchingInteractor,
    private var searchingHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private var results: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()

    private var searchingLiveData =
        MutableLiveData<StatesOfSearching>(StatesOfSearching.Search)

    private var searchingHistoryList: MutableLiveData<List<Track>> =
        MutableLiveData<List<Track>>().apply {
            value = emptyList()
        }

    fun getSearchingLiveData(): LiveData<StatesOfSearching> {
        return searchingLiveData
    }

    private val searchingTrackConsumer = object : SearchingInteractor.SearchingTrackConsumer {
        override fun consume(tracks: List<Track>?, errorMessage: ErrorType?) {
            when (errorMessage) {
                ErrorType.CONNECTION_ERROR -> searchingLiveData.postValue(StatesOfSearching.ErrorConnection)
                ErrorType.SERVER_ERROR -> searchingLiveData.postValue(StatesOfSearching.ErrorFound)

                else -> {
                    results.postValue(tracks)
                    searchingLiveData.postValue(
                        if (tracks.isNullOrEmpty())
                            StatesOfSearching.ErrorFound
                        else StatesOfSearching.SearchCompleted(tracks)
                    )
                }
            }
        }
    }

    fun requestSearch(expression: String) {
        searchingLiveData.postValue(StatesOfSearching.Loading)
        searchingInteractor.search(expression, searchingTrackConsumer)
    }

    fun add(it: Track) {
        searchingHistoryInteractor.add(it)
    }

    fun clearHistory() {
        searchingHistoryInteractor.clearHistory()
    }

    fun provideSearchHistory(): LiveData<List<Track>> {
        val history = searchingHistoryInteractor.provideSearchHistory()
        searchingHistoryList.value = searchingHistoryInteractor.provideSearchHistory()
        if (history.isNullOrEmpty()) {
            searchingHistoryList.postValue(emptyList())
        }
        return searchingHistoryList
    }

    fun clearSearchingHistoryList() {
        results.value = emptyList()
        searchingHistoryList.value = searchingHistoryInteractor.provideSearchHistory()
        searchingLiveData.value =
            searchingHistoryList.value?.let { StatesOfSearching.SearchAndHistory(it) }
    }
}