package com.example.playlistmaker.data.playlist

import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.dbPlaylist.PlaylistDatabase
import com.example.playlistmaker.data.dbTrack.TrackDatabase
import com.example.playlistmaker.domain.playlist.interactor.PlaylistRepository
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val trackDatabase: TrackDatabase,
    private val playlistDatabase: PlaylistDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : PlaylistRepository {

    override fun createPlaylist(title: String, description: String?, uri: String) {
        val playlist = Playlist(0, title, description, uri, emptyList())
        playlistDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override fun getPlaylist(): Flow<List<Playlist>> = playlistDatabase.playlistDao().getPlaylist()
        .map { playlistEntityList ->
            playlistEntityList.map { playlistDbConvertor.map(it) }
        }

    override fun edit(track: Track, playlist: Playlist) {
        playlistDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
        trackDatabase.trackListingDao().insertTrack(track)
    }
}