package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerStateChangeListener
import com.example.playlistmaker.domain.player.StatesOfPlaying
import java.text.SimpleDateFormat
import java.util.*

class PlayerRepositoryImpl: PlayerRepository {

    companion object{
        const val AUDIO_DELAY_MILLIS = 100L
    }

    private val mediaPlayer = MediaPlayer()
    private lateinit var listener: PlayerStateChangeListener
    private var statesOfPlaying = StatesOfPlaying.STATE_DEFAULT
    private val timeRunnable: Runnable = Runnable { duration() }
    var time = "00:00"
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())

    override fun preparePlayer(url: String, completion: ()->Unit) {
        if (statesOfPlaying != StatesOfPlaying.STATE_DEFAULT) return
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            statesOfPlaying = StatesOfPlaying.STATE_PREPARED
            completion()
        }
        mediaPlayer.setOnCompletionListener {
            statesOfPlaying = StatesOfPlaying.STATE_PREPARED
            mainThreadHandler?.removeCallbacks(timeRunnable)
            time = "00:00"
        }
    }

    override fun play() {
        mediaPlayer.start()
        statesOfPlaying = StatesOfPlaying.STATE_PLAYING
        duration()
    }

    override fun setListener(listener: PlayerStateChangeListener) {
        this.listener = listener
    }

    override fun pause() {
        mediaPlayer.pause()
        statesOfPlaying = StatesOfPlaying.STATE_PAUSED
        mainThreadHandler?.removeCallbacks(timeRunnable)
    }

    override fun destroy() {
        if(statesOfPlaying == StatesOfPlaying.STATE_DEFAULT) {
            mediaPlayer.release()
            mainThreadHandler?.removeCallbacks(timeRunnable)
        }
    }

    private fun duration() {
        if((statesOfPlaying == StatesOfPlaying.STATE_PLAYING)or(statesOfPlaying == StatesOfPlaying.STATE_PAUSED)){
            time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            mainThreadHandler?.postDelayed(timeRunnable, AUDIO_DELAY_MILLIS)
        }
        else{
            time = "00:00"
            mainThreadHandler?.postDelayed(timeRunnable, AUDIO_DELAY_MILLIS)
        }
    }

    override fun time(): String {
        return time
    }

    override fun stateReporter(): StatesOfPlaying {
        return statesOfPlaying
    }
}