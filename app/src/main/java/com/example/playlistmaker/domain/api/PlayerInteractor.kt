package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.StatesOfPlaying

interface PlayerInteractor {
    fun pause()
    fun play()
    fun destroy()
    fun createPlayer(url: String, completion: ()->Unit)
    fun time():String
    fun stateListener():StatesOfPlaying
}