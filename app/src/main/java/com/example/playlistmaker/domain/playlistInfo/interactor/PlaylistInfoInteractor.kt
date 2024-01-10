package com.example.playlistmaker.domain.playlistInfo.interactor

import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInfoInteractor {
    fun getTrack(playlist: Playlist): Flow<List<Track>>
    fun duration(playlist: Playlist): Flow<String>
}