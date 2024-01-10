package com.example.playlistmaker.ui.playlist.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.playlist.interactor.ImagesInteractor
import com.example.playlistmaker.domain.playlist.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.search.model.Playlist

class EditPlaylistViewModel(private val playlistInteractor: PlaylistInteractor, private val imagesInteractor: ImagesInteractor) : ViewModel() {
    fun deletePlaylist(playlist: Playlist) {
        playlistInteractor.deletePlaylist(playlist)
    }

    fun savePlaylist(
        playlist: Playlist,
        playlistName: String,
        description: String?,
        uri: String
    ) {
        playlistInteractor.save(playlist, playlistName, description, uri)
    }

    fun saveImageToPrivateStorage(uri: Uri): Uri {
        return imagesInteractor.saveImageToPrivateStorage(uri)
    }
}