package com.example.playlistmaker.domain.player

import com.example.playlistmaker.ui.player.view_model.ViewModelAudioPlayer

class PlayerStateChangeListenerImpl(private val viewModelAudioPlayer: ViewModelAudioPlayer): PlayerStateChangeListener {
    override fun onChange(state: PlayerState) {
        viewModelAudioPlayer.playerStateLiveData.postValue(state)
    }
}