package com.example.playlistmaker.data.dbPlaylist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val playlistName: String,
    val playlistDescription: String?,
    val playlistUri: String,
    val trackList: String?
)