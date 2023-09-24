package com.example.playlistmaker.presentation.ui.activities

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.StatesOfPlaying
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.presentation.ui.AudioPlayerActivityPresentation
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity(), AudioPlayerActivityPresentation {

    private lateinit var audioPlayerTrackName: TextView
    private lateinit var audioPlayerArtistName: TextView
    private lateinit var audioPlayerTime: TextView
    private lateinit var audioPlayerAlbumName: TextView
    private lateinit var audioPlayerYearNumber: TextView
    private lateinit var audioPlayerGenreName: TextView
    private lateinit var audioPlayerCountryName: TextView
    private lateinit var audioPlayerCover: ImageView
    private lateinit var audioPlayerBackButton: ImageButton
    private lateinit var audioPlayerPlayButton: ImageButton
    private lateinit var audioPlayerTrackTimer: TextView

    lateinit var playerInteractor: PlayerInteractor
    private var mainThreadHandler: Handler? = null
    lateinit var statesOfPlaying: StatesOfPlaying

    companion object {
        const val KEY_TRACK_NAME = "Track Name"
        const val KEY_TRACK_ARTIST = "Artist Name"
        const val KEY_TRACK_TIME = "Track Time"
        const val KEY_TRACK_ALBUM = "Album"
        const val KEY_TRACK_YEAR = "Year"
        const val KEY_TRACK_GENRE = "Genre"
        const val KEY_TRACK_COUNTRY = "Country"
        const val KEY_TRACK_COVER = "Cover"
        const val year = 4
        const val URL = "Url"
        const val AUDIO_DELAY_MILLIS = 100L
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player)
        initViews()
        playerInteractor = Creator.providePlayerInteractor()
        statesOfPlaying = StatesOfPlaying.STATE_PAUSED
        audioPlayerPlayButton.isEnabled = false
        audioPlayerPlayButton.setOnClickListener { playBackControl() }
        mainThreadHandler?.post(changeButton())
        mainThreadHandler?.post(changeTimer())
        audioPlayerBackButton.setOnClickListener {
            finish()
        }

        audioPlayerTrackName.text =
            intent.extras?.getString(KEY_TRACK_NAME) ?: getString(R.string.unknownTrack)
        audioPlayerArtistName.text =
            intent.extras?.getString(KEY_TRACK_ARTIST) ?: getString(R.string.unknownArtist)
        audioPlayerTime.text =
            intent.extras?.getString(KEY_TRACK_TIME) ?: getString(R.string.track_time)
        audioPlayerAlbumName.text =
            intent.extras?.getString(KEY_TRACK_ALBUM) ?: getString(R.string.unknownAlbum)
        audioPlayerYearNumber.text =
            (intent.extras?.getString(KEY_TRACK_YEAR) ?: getString(R.string.unknownYear)).take(year)
        audioPlayerGenreName.text =
            intent.extras?.getString(KEY_TRACK_GENRE) ?: getString(R.string.unknownGenre)
        audioPlayerCountryName.text =
            intent.extras?.getString(KEY_TRACK_COUNTRY) ?: getString(R.string.unknownCountry)

        val image = (intent.extras?.getString(KEY_TRACK_COVER))

        if (image != null) {
            image.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(image)
                .placeholder(R.drawable.audio_player_cover)
                .centerCrop()
                .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.icon_padding)))
                .into(audioPlayerCover)
        }

        val url = intent.extras?.getString(URL)
        if (!url.isNullOrEmpty()) playerInteractor.createPlayer(url){
                audioPlayerPlayButton.isEnabled = true
            }
    }

    override fun preparePlayer() {
            audioPlayerPlayButton.isEnabled = true
            audioPlayerPlayButton.setImageResource(R.drawable.play_button)
    }

    private fun playBackControl() {
        when (statesOfPlaying) {
            StatesOfPlaying.STATE_PLAYING -> {
                playerInteractor.pause()
            }
            StatesOfPlaying.STATE_PREPARED, StatesOfPlaying.STATE_PAUSED -> {
                playerInteractor.play()
            }
            else -> {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.destroy()
    }

    private fun initViews() {
        audioPlayerTrackName = findViewById(R.id.audioPlayerTrackName)
        audioPlayerArtistName = findViewById(R.id.audioPlayerArtistName)
        audioPlayerTime = findViewById(R.id.audioPlayerTime)
        audioPlayerAlbumName = findViewById(R.id.audioPlayerAlbumName)
        audioPlayerYearNumber = findViewById(R.id.audioPlayerYearNumber)
        audioPlayerGenreName = findViewById(R.id.audioPlayerGenreName)
        audioPlayerCountryName = findViewById(R.id.audioPlayerCountryName)
        audioPlayerCover = findViewById(R.id.audioPlayerCover)
        audioPlayerBackButton = findViewById(R.id.audioPlayerBackButton)
        audioPlayerPlayButton = findViewById(R.id.audioPlayerPlayButton)
        audioPlayerTrackTimer = findViewById(R.id.audioPlayerTrackTimer)
        mainThreadHandler = Handler(Looper.getMainLooper())
    }

    override fun playerVisibility() {
        statesOfPlaying = playerInteractor.stateListener()

        when(statesOfPlaying){
            StatesOfPlaying.STATE_PAUSED -> audioPlayerPlayButton.setImageResource(R.drawable.play_button)
            StatesOfPlaying.STATE_DEFAULT -> audioPlayerPlayButton.setImageResource(R.drawable.play_button)
            StatesOfPlaying.STATE_PREPARED -> audioPlayerPlayButton.setImageResource(R.drawable.play_button)
            StatesOfPlaying.STATE_PLAYING -> audioPlayerPlayButton.setImageResource(R.drawable.audio_player_pause)
        }
    }

    private fun changeButton():Runnable{
        val changing = Runnable {
            playerVisibility()
            mainThreadHandler?.postDelayed(changeButton(), AUDIO_DELAY_MILLIS)
        }
        return changing
    }

    private fun changeTimer():Runnable{
        val changing = Runnable{
            audioPlayerTrackTimer.text = playerInteractor.time()
            mainThreadHandler?.postDelayed(changeTimer(), AUDIO_DELAY_MILLIS)
        }
        return changing
    }
}