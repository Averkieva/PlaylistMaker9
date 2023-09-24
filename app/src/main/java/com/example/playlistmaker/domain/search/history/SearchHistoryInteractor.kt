package com.example.playlistmaker.domain.search.history

import com.example.playlistmaker.domain.search.model.Track

interface SearchHistoryInteractor {

    fun clearHistory()

    fun add(it:Track)

    fun provideSearchHistory():List<Track>
}