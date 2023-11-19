package com.example.playlistmaker.domain.player

import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun pause()
    fun play()
    fun destroy()
    fun createPlayer(url: String, completion: () -> Unit)
    fun time(): Flow<String>
    fun setListener(listener: PlayerStateChangeListener)
}