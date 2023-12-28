package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.PlayerStateChangeListener
import com.example.playlistmaker.domain.player.StatesOfPlaying
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewModelAudioPlayer(private val playerInteractor: PlayerInteractor) : ViewModel() {

    companion object {
        const val AUDIO_DELAY_MILLIS = 300L
    }

    private val playerStateLiveData = MutableLiveData<PlayerScreenState>()
    var audioPlayerJob: Job? = null
    var time = MutableLiveData("00:00")

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
}