package com.example.playlistmaker.domain.search.history

import com.example.playlistmaker.domain.search.model.Track

interface SearchingHistory {

    fun clearHistory()

    fun add(newIt: Track)

    fun provideSearchHistory():List<Track>
}