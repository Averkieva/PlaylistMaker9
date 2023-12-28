package com.example.playlistmaker.domain.favourite

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTrackInteractor {
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getFromFavourite(): Flow<List<Track>>
    fun isLiked(trackId: Long): Flow<Boolean>
}