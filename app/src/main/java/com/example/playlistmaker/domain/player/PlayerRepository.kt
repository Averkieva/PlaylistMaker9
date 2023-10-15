package com.example.playlistmaker.domain.player

interface PlayerRepository {
    fun pause()
    fun play()
    fun destroy()
    fun preparePlayer(url:String, completion: ()->Unit)
    fun time():String
    fun setListener(listener: PlayerStateChangeListener)
}