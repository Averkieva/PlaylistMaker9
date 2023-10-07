package com.example.playlistmaker.domain.player

class PlayerState (val playingState: StatesOfPlaying, val timeTrack: String){
}
enum class StatesOfPlaying{
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED
}

