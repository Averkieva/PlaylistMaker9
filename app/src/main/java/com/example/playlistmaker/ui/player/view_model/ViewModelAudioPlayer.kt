package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.PlayerStateChangeListener
import com.example.playlistmaker.domain.player.StatesOfPlaying

class ViewModelAudioPlayer(private val playerInteractor: PlayerInteractor) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerScreenState>()

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
    }

    fun pause() {
        playerInteractor.pause()
    }

    fun destroy() {
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
}