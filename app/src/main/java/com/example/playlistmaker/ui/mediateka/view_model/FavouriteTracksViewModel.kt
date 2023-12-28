package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourite.FavouriteTrackInteractor
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavouriteTracksViewModel(
    private val favouriteTrackInteractor: FavouriteTrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    companion object {
        const val DELAY_MILLIS = 300L
    }

    var results: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()

    fun createFavourite(): LiveData<List<Track>?> {
        viewModelScope.launch {
            while (true) {
                delay(DELAY_MILLIS)
                favouriteTrackInteractor.getFromFavourite()
                    .collect { trackList ->
                        if (!trackList.isNullOrEmpty()) {
                            results.postValue(trackList)
                        } else results.postValue(emptyList())
                    }
            }
        }
        return results
    }

    fun add(track: Track) {
        searchHistoryInteractor.add(track)
    }
}