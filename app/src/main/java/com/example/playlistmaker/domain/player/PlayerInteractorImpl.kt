package com.example.playlistmaker.domain.player

import com.example.playlistmaker.creator.Creator

class PlayerInteractorImpl: PlayerInteractor {
    var rep = Creator.providePlayerRepository()

    override fun pause() {
        rep.pause()
    }

    override fun play() {
        rep.play()
    }

    override fun destroy() {
        rep.destroy()
    }

    override fun createPlayer(url: String, completion: ()->Unit) {
        rep.preparePlayer(url, completion)
    }

    override fun time(): String {
        return rep.time()
    }

    override fun setListener(listener: PlayerStateChangeListener) {
        rep.setListener(listener)
    }

}