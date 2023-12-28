package com.example.playlistmaker.domain.search.interactors

import com.example.playlistmaker.data.search.request_response.Resource
import com.example.playlistmaker.domain.search.RepositoryTrack
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchingInteractorImpl(private val repository: RepositoryTrack) : SearchingInteractor {

    override fun search(expression: String): Flow<Resource<List<Track>>> {
        return repository.searching(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    (Resource.Success(result.data))
                }

                is Resource.Error<*> -> {
                    Resource.Error(null, result.message)
                }
            } as Resource<List<Track>>
        }
    }
}
