package com.example.playlistmaker.ui.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.search.model.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val playlistListLiveData: MutableLiveData<List<Playlist>> = MutableLiveData<List<Playlist>>()

    fun createPlaylist() {
        viewModelScope.launch {
            playlistInteractor.getPlaylist().collect {
                if (it.isNotEmpty()) {
                    playlistListLiveData.postValue(it)
                } else {
                    playlistListLiveData.postValue(emptyList())
                }
            }
        }
    }

    fun getPlayerStateLiveData(): LiveData<List<Playlist>> = playlistListLiveData
}