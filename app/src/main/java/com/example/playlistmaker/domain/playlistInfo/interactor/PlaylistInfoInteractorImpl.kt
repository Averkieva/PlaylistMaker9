package com.example.playlistmaker.domain.playlistInfo.interactor

import com.example.playlistmaker.domain.playlistInfo.repository.PlaylistInfoRepository
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInfoInteractorImpl(private val repository: PlaylistInfoRepository) :
    PlaylistInfoInteractor {

    override fun getTrack(playlist: Playlist): Flow<List<Track>> {
        return repository.getTrack(playlist)
    }

    override fun duration(playlist: Playlist): Flow<String> {
        return repository.duration(playlist)
    }
}