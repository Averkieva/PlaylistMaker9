package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.interactors.SearchingInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.view_model.states.StatesOfSearching

class ViewModelSearching(private var searchingInteractor: SearchingInteractor, private var searchingHistoryInteractor: SearchHistoryInteractor):ViewModel() {

    private var results: MutableLiveData<List<Track>> = MutableLiveData<List<Track>>()

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
        override fun consume(tracks: List<Track>) {
            results.postValue(tracks)
            searchingLiveData.postValue(
                if (tracks.isNullOrEmpty())
                    StatesOfSearching.ErrorFound
                else StatesOfSearching.SearchCompleted(tracks)
            )
        }
    }

    fun requestSearch(expression: String) {
        searchingLiveData.postValue(StatesOfSearching.Loading)
        try {
            searchingInteractor.search(expression, searchingTrackConsumer)
        } catch (error: Error) {
            searchingLiveData.postValue(StatesOfSearching.ErrorConnection)
        }
    }

    fun add(it: Track){
        searchingHistoryInteractor.add(it)
    }

    fun clearHistory(){
        searchingHistoryInteractor.clearHistory()
    }

    fun provideSearchHistory(): LiveData<List<Track>>{
        searchingHistoryList.value = searchingHistoryInteractor.provideSearchHistory()
        if (searchingHistoryInteractor.provideSearchHistory().isNullOrEmpty()) {
            searchingHistoryList.postValue(emptyList())
        }
        return searchingHistoryList
    }

    fun clearSearchingHistoryList() {
        results.value = emptyList()
        searchingLiveData.value= searchingHistoryList.value?.let { StatesOfSearching.SearchAndHistory(it) }
    }



    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ViewModelSearching(
                        Creator.provideSearchingInteractor(),Creator.provideSearchHistoryInteractor()
                    ) as T
                }
            }
    }
}