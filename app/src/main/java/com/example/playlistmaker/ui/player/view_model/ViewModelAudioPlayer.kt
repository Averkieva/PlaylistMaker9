package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.PlayerStateChangeListener
import com.example.playlistmaker.domain.player.StatesOfPlaying

class ViewModelAudioPlayer(private val playerInteractor: PlayerInteractor):ViewModel() {

    val playerStateLiveData = MutableLiveData( PlayerState(StatesOfPlaying.STATE_PAUSED, ""))

    init{
        val listener = object: PlayerStateChangeListener{
            override fun onChange(state: PlayerState) {
                playerStateLiveData.postValue(state)
            }
        }
        playerInteractor.setListener(listener)
    }

    fun createPlayer(url: String, completion: ()->Unit) {
        playerInteractor.createPlayer(url, completion)
    }

    fun play(){
        playerInteractor.play()
    }

    fun pause(){
        playerInteractor.pause()
    }

    fun destroy(){
        playerInteractor.destroy()
    }

    fun time():String{
        return playerInteractor.time()
    }

     fun playBackControl() {
         val currentState = playerStateLiveData.value
        when (currentState?.playingState) {
            StatesOfPlaying.STATE_PLAYING -> {
                pause()
            }
            StatesOfPlaying.STATE_PREPARED, StatesOfPlaying.STATE_PAUSED -> {
                play()
            }
            else -> {}
        }
    }

    companion object{
        fun getViewModelFactory(): ViewModelProvider.Factory = object: ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T: ViewModel> create (modelClass: Class<T>):T{
                return ViewModelAudioPlayer(
                    Creator.providePlayerInteractor()
                ) as T
            }
        }
    }
}