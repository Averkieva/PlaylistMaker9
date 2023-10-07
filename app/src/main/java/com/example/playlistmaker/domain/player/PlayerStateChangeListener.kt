package com.example.playlistmaker.domain.player

interface PlayerStateChangeListener {
    fun onChange(state:PlayerState)
}