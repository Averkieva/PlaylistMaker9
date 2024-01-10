package com.example.playlistmaker.domain.playlist.interactor

import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override fun createPlaylist(
        playlistName: String,
        playlistDescription: String?,
        playlistUri: String
    ) {
        repository.createPlaylist(playlistName, playlistDescription, playlistUri)
    }

    override fun getPlaylist(): Flow<List<Playlist>> {
        return repository.getPlaylist()
    }

    override fun edit(track: Track, playlist: Playlist) {
        repository.edit(track, playlist)
    }
}