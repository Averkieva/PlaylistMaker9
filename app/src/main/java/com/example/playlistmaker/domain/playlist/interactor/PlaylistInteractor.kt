package com.example.playlistmaker.domain.playlist.interactor

import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun createPlaylist(playlistName: String, playlistDescription: String?, playlistUri: String)

    fun getPlaylist(): Flow<List<Playlist>>

    fun edit(track: Track, playlist: Playlist)
}