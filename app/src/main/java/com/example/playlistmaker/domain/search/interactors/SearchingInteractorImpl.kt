package com.example.playlistmaker.domain.search.interactors

import com.example.playlistmaker.domain.search.RepositoryTrack
import com.example.playlistmaker.domain.search.model.Track

class SearchingInteractorImpl(private val repository: RepositoryTrack) : SearchingInteractor {

    override fun search(
        expression: String,
        consumer: SearchingInteractor.SearchingTrackConsumer
    ) {
        var data: List<Track>
        val t = Thread {
            data = repository.searching(expression)
            consumer.consume(data)
        }
        t.start()
    }
}