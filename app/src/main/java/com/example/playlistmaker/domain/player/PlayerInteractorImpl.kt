package com.example.playlistmaker.domain.player

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

    override fun time(): String {
        return rep.time()
    }

    override fun setListener(listener: PlayerStateChangeListener) {
        rep.setListener(listener)
    }

}