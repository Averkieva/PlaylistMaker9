package com.example.playlistmaker.domain.search.interactors

import com.example.playlistmaker.data.search.request_response.Resource
import com.example.playlistmaker.domain.search.RepositoryTrack
import java.util.concurrent.Executors

class SearchingInteractorImpl(private val repository: RepositoryTrack) : SearchingInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(
        expression: String,
        consumer: SearchingInteractor.SearchingTrackConsumer
    ) {
        executor.execute {
            when (val tracksData=repository.searching(expression))  {
                is Resource.Success -> { consumer.consume(tracksData.data, null) }
                is Resource.Error -> { consumer.consume(null, tracksData.message) }
            }
        }
    }
}