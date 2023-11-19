package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.request_response.NetworkClient
import com.example.playlistmaker.data.search.request_response.Resource
import com.example.playlistmaker.data.search.request_response.SearchRequest
import com.example.playlistmaker.data.search.request_response.TrackResponse
import com.example.playlistmaker.domain.search.ErrorType
import com.example.playlistmaker.domain.search.RepositoryTrack
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*

class RepositoryTrackImpl(private val network: NetworkClient) : RepositoryTrack {

    override fun searching(expression: String): Flow<Resource<List<Track>>> = flow {
        try {
            val response = network.doRequest(SearchRequest(expression))
            when (response.resultCode) {
                -1 -> {
                    emit(Resource.Error(ErrorType.CONNECTION_ERROR))
                }
                200 -> {
                    emit(Resource.Success((response as TrackResponse).results.map {
                        Track(
                            it.trackName,
                            it.artistName,
                            SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(it.trackTimeMillis),
                            it.artworkUrl100,
                            it.trackId,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }))
                }
                else -> {
                    emit(Resource.Error(ErrorType.SERVER_ERROR))
                }
            }
        } catch (error: Error) {
            throw Exception(error)
        }
    }
}