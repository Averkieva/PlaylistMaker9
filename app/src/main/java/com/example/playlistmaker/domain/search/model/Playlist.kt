package com.example.playlistmaker.domain.search.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Int = 0,
    val playlistName: String,
    val playlistDescription: String?,
    val playlistUri: String,
    var trackList: List<Long?>
) : Parcelable