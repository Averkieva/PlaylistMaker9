package com.example.playlistmaker.ui.playlistInfo.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.playlistInfo.interactor.PlaylistInfoInteractor
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.settings.interactors.SettingsInteractor
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val playlistInfoInteractor: PlaylistInfoInteractor
) : ViewModel() {

    private val tracks: MutableLiveData<List<Track>> = MutableLiveData(emptyList())

    fun getTracksLiveData(): LiveData<List<Track>> = tracks
    fun getTrack(playlist: Playlist) {
        viewModelScope.launch {
            playlistInfoInteractor.getTrack(playlist).collect { list ->
                tracks.postValue(list)
            }
        }
    }

    private val duration: MutableLiveData<String> = MutableLiveData("")

    fun getDuration(): LiveData<String> = duration

    fun getTime(playlist: Playlist) {
        viewModelScope.launch {
            playlistInfoInteractor.duration(playlist).collect { finishTime ->
                duration.postValue(finishTime)
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        playlistInteractor.deletePlaylist(playlist)
    }

    private val edit: MutableLiveData<Playlist> = MutableLiveData()

    fun getEdit(): LiveData<Playlist> = edit

    fun getPlaylist(searchId: Int) {
        viewModelScope.launch {
            playlistInteractor.searchPlaylist(searchId).collect {
                edit.postValue(it)
            }
        }
    }

    fun deleteTrack(track: Track, playlist: Playlist) {
        playlist.trackList = playlist.trackList.filter { it != track.trackId }
        playlistInteractor.edit(track, playlist)
    }

    fun isDark(): Boolean {
        return settingsInteractor.isDark()
    }
}