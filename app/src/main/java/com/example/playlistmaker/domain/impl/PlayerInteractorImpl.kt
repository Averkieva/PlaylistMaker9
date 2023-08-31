package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.StatesOfPlaying
import com.example.playlistmaker.domain.api.PlayerInteractor

class PlayerInteractorImpl:PlayerInteractor {
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

    override fun stateListener(): StatesOfPlaying {
        return rep.stateReporter()
    }
}