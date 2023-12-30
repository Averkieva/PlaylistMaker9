package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.FavouriteEntity
import com.example.playlistmaker.domain.search.model.Track

class TrackDbConvertor {
    fun map(track: Track): FavouriteEntity {
        return FavouriteEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.durationTime,
            track.isFavourite
        )
    }

    fun map(track: FavouriteEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.durationTime,
            track.isFavourite
        )
    }
}