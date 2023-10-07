package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.StatesOfPlaying

interface PlayerRepository {
    fun pause()
    fun play()
    fun destroy()
    fun preparePlayer(url:String, completion: ()->Unit)
    fun time():String
    fun stateReporter(): StatesOfPlaying
    fun setListener(listener: PlayerStateChangeListener)
}