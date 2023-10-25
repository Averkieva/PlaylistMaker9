package com.example.playlistmaker.domain.search.interactors

import com.example.playlistmaker.domain.search.ErrorType
import com.example.playlistmaker.domain.search.model.Track

interface SearchingInteractor {

    fun search(expression: String, consumer: SearchingTrackConsumer)

    interface SearchingTrackConsumer {
        fun consume(tracks: List<Track>?, errorMessage: ErrorType?)
    }
}