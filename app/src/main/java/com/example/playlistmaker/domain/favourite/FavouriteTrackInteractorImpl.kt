package com.example.playlistmaker.domain.favourite

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class FavouriteTrackInteractorImpl(private val favouriteTrackRepository: FavouriteTrackRepository) :
    FavouriteTrackInteractor {
    override suspend fun insertTrack(track: Track) {
        favouriteTrackRepository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favouriteTrackRepository.deleteTrack(track)
    }

    override fun getFromFavourite(): Flow<List<Track>> {
        return favouriteTrackRepository.getFromFavourite()
    }

    override fun isLiked(trackId: Long): Flow<Boolean> {
        return favouriteTrackRepository.isLiked(trackId)
    }

}