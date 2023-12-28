package com.example.playlistmaker.domain.player

import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(private val rep: PlayerRepository) : PlayerInteractor {

    override fun pause() {
        rep.pause()
    }

    override fun play() {
        rep.play()
    }

    override fun destroy() {
        rep.destroy()
    }

    override fun createPlayer(url: String, completion: () -> Unit) {
        rep.preparePlayer(url, completion)
    }

    override fun time(): Flow<String> {
        return rep.duration()
    }

    override fun setListener(listener: PlayerStateChangeListener) {
        rep.setListener(listener)
    }

}