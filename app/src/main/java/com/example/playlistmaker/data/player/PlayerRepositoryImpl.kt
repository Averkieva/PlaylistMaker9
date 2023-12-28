package com.example.playlistmaker.data.player

import android.annotation.SuppressLint
import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.PlayerStateChangeListener
import com.example.playlistmaker.domain.player.StatesOfPlaying
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat

class PlayerRepositoryImpl : PlayerRepository {

    companion object {
        const val AUDIO_DELAY_MILLIS = 300L
    }

    private val mediaPlayer = MediaPlayer()
    private lateinit var listener: PlayerStateChangeListener
    private var statesOfPlaying = StatesOfPlaying.STATE_DEFAULT
    var time = "00:00"
    private var audioPlayerJob: Job? = null

    override fun preparePlayer(url: String, completion: () -> Unit) {
        if (statesOfPlaying != StatesOfPlaying.STATE_DEFAULT) return
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            statesOfPlaying = StatesOfPlaying.STATE_PREPARED
            completion()
            listener.onChange(PlayerState(statesOfPlaying, time))
            audioPlayerJob?.start()
        }
        mediaPlayer.setOnCompletionListener {
            statesOfPlaying = StatesOfPlaying.STATE_PREPARED
            listener.onChange(PlayerState(statesOfPlaying, time))
        }
    }

    override fun play() {
        mediaPlayer.start()
        statesOfPlaying = StatesOfPlaying.STATE_PLAYING
        listener.onChange(PlayerState(statesOfPlaying, time))
    }

    override fun setListener(listener: PlayerStateChangeListener) {
        this.listener = listener
    }

    override fun pause() {
        mediaPlayer.pause()
        statesOfPlaying = StatesOfPlaying.STATE_PAUSED
        listener.onChange(PlayerState(statesOfPlaying, time))
    }

    override fun destroy() {
        if (statesOfPlaying == StatesOfPlaying.STATE_DEFAULT)
            mediaPlayer.release()
        listener.onChange(PlayerState(statesOfPlaying, time))
        audioPlayerJob?.cancel()
    }

    @SuppressLint("SimpleDataFormat")
    override fun duration(): Flow<String> = flow {
        val simpleDataFormat = SimpleDateFormat("mm:ss")
        while (true) {
            if ((statesOfPlaying == StatesOfPlaying.STATE_PLAYING) or (statesOfPlaying == StatesOfPlaying.STATE_PAUSED)) {
                emit(simpleDataFormat.format(mediaPlayer.currentPosition))
            } else {
                emit("00:00")
            }
            delay(AUDIO_DELAY_MILLIS)
        }
    }
}