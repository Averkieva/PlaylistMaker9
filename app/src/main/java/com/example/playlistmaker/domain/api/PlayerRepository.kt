package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.StatesOfPlaying

interface PlayerRepository {
    fun pause()
    fun play()
    fun destroy()
    fun preparePlayer(url:String, completion: ()->Unit)
    fun time():String
    fun stateReporter():StatesOfPlaying
}