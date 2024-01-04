package com.example.playlistmaker.data.dbTrack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: Long?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val durationTime: Long?,
    val isFavourite: Boolean = false
)