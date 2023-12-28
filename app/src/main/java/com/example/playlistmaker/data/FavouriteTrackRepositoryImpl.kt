package com.example.playlistmaker.data

import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.domain.favourite.FavouriteTrackRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavouriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavouriteTrackRepository {
    override suspend fun insertTrack(track: Track) {
        track.isFavourite = true
        track.durationTime = System.currentTimeMillis()
        val entityTrack = trackDbConvertor.map(track)
        appDatabase.trackDao().insertTrack(entityTrack)
    }

    override suspend fun deleteTrack(track: Track) {
        track.isFavourite = false
        trackDbConvertor.map(track).let {
            appDatabase.trackDao().deleteTrack(it)
        }
    }

    override fun getFromFavourite(): Flow<List<Track>> =
        appDatabase.trackDao().getTrack().map { favoriteTracks ->
            favoriteTracks.map { favoriteTrack -> trackDbConvertor.map(favoriteTrack) }
        }

    override fun isLiked(trackId: Long): Flow<Boolean> = flow {
        emit(appDatabase.trackDao().getTrackId(trackId) != null)
    }
}