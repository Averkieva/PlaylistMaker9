package com.example.playlistmaker.domain.search.history

import com.example.playlistmaker.domain.search.model.Track

class SearchHistoryInteractorImpl(private val repository: SearchingHistory): SearchHistoryInteractor {

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun add(it: Track) {
        repository.add(it)
    }

    override fun provideSearchHistory(): List<Track> {
        return repository.provideSearchHistory()
    }
}