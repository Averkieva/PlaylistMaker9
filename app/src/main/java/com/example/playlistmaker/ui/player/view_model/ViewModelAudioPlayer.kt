package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.StatesOfPlaying

class ViewModelAudioPlayer(private val playerInteractor: PlayerInteractor):ViewModel() {

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

    fun stateListener(): StatesOfPlaying{
        return playerInteractor.stateListener()
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