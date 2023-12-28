package com.example.playlistmaker.domain.player

import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun pause()
    fun play()
    fun destroy()
    fun preparePlayer(url: String, completion: () -> Unit)
    fun duration(): Flow<String>
    fun setListener(listener: PlayerStateChangeListener)
}