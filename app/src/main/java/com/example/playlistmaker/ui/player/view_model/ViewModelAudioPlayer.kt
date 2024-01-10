package com.example.playlistmaker.ui.player.view_model

import android.app.Application
import androidx.lifecycle.*
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.favourite.FavouriteTrackInteractor
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.PlayerStateChangeListener
import com.example.playlistmaker.domain.player.StatesOfPlaying
import com.example.playlistmaker.domain.playlist.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewModelAudioPlayer(
    private val playerInteractor: PlayerInteractor,
    private val favouriteTrackInteractor: FavouriteTrackInteractor,
    private val playlistInteractor: PlaylistInteractor,
    application: Application
) : AndroidViewModel(application) {

    private val playlist: MutableLiveData<List<Playlist>> = MutableLiveData<List<Playlist>>(emptyList())
    private val playerStateLiveData = MutableLiveData<PlayerScreenState>()
    private val time = MutableLiveData(application.resources.getString(R.string.track_time))
    private val isFavourite = MutableLiveData<Boolean>()

    var audioPlayerJob: Job? = null
    var favouriteTrackJob: Job? = null

    init {
        val listener = object : PlayerStateChangeListener {
            override fun onChange(state: PlayerState) {
                val isPlaying = state.playingState == StatesOfPlaying.STATE_PLAYING

                playerStateLiveData.postValue(PlayerScreenState(isPlaying, state.timeTrack))
            }
        }
        playerInteractor.setListener(listener)
    }

    fun getPlayerStateLiveData(): LiveData<PlayerScreenState> = playerStateLiveData

    fun createPlayer(url: String, completion: () -> Unit) {
        playerInteractor.createPlayer(url, completion)
    }

    fun play() {
        playerInteractor.play()
        audioPlayerJob?.start()
    }

    fun pause() {
        playerInteractor.pause()
    }

    fun destroy() {
        audioPlayerJob?.cancel()
        playerInteractor.destroy()
    }

    fun playBackControl() {
        val currentState = playerStateLiveData.value
        if (currentState?.isPlaying == true) {
            pause()
        } else {
            play()
        }
    }

    fun standTime(): LiveData<String> {
        audioPlayerJob = viewModelScope.launch {
            while (true) {
                delay(AUDIO_DELAY_MILLIS)
                playerInteractor.time().collect {
                    time.postValue(it)
                }
            }
        }
        return time
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavourite)
                track.trackId?.let { favouriteTrackInteractor.deleteTrack(track) }
            else
                track.trackId?.let { favouriteTrackInteractor.insertTrack(track) }
        }
    }

    fun observeFavourite(track: Track): LiveData<Boolean> {
        favouriteTrackJob = viewModelScope.launch {
            while (true) {
                delay(AUDIO_DELAY_MILLIS)
                track.trackId?.let { trackId ->
                    favouriteTrackInteractor.isLiked(trackId).collect { item ->
                        isFavourite.postValue(item)
                    }
                }
            }
        }
        return isFavourite
    }

    val inserted = MutableLiveData(false)

    fun add(track: Track, playlist: Playlist) {
        if (playlist.trackList.contains(track.trackId)) {
            inserted.postValue(true)
        } else {
            inserted.postValue(false)

            val newTrackList = (playlist.trackList + track.trackId)
            playlist.trackList = newTrackList
            playlistInteractor.edit(track, playlist)
        }
    }

    fun createPlaylist(): LiveData<List<Playlist>> {
        viewModelScope.launch {
            playlistInteractor.getPlaylist()
                .collect {
                    if (it.isNotEmpty()) {
                        playlist.postValue(it)
                    } else {
                        playlist.postValue(emptyList())
                    }
                }
        }
        return playlist
    }

    companion object {
        const val AUDIO_DELAY_MILLIS = 300L
    }
}