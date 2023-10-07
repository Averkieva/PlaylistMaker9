package com.example.playlistmaker.domain.player

interface PlayerInteractor {
    fun pause()
    fun play()
    fun destroy()
    fun createPlayer(url: String, completion: ()->Unit)
    fun time():String
    fun stateListener(): StatesOfPlaying
    fun setListener(listener: PlayerStateChangeListener)
}