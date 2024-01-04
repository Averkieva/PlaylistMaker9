package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.dbPlaylist.PlaylistEntity
import com.example.playlistmaker.domain.search.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor {

    private val gson = Gson()

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistUri,
            gson.toJson(playlist.trackList)
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistUri,
            gson.fromJson(playlist.trackList, object : TypeToken<List<Long>>() {}.type)
        )
    }
}