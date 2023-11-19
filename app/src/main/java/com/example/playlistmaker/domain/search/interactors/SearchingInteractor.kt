package com.example.playlistmaker.domain.search.interactors

import com.example.playlistmaker.data.search.request_response.Resource
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchingInteractor {

    fun search(expression: String): Flow<Resource<List<Track>>>
}