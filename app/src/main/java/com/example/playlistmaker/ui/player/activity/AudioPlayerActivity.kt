package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AudioPlayerBinding
import com.example.playlistmaker.domain.player.StatesOfPlaying
import com.example.playlistmaker.ui.player.view_model.ViewModelAudioPlayer

class AudioPlayerActivity : AppCompatActivity() {

    private var mainThreadHandler: Handler? = null
    private lateinit var viewModelAudioPlayer: ViewModelAudioPlayer
    private lateinit var binding: AudioPlayerBinding
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
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelAudioPlayer = ViewModelProvider(
            this,
            ViewModelAudioPlayer.getViewModelFactory()
        )[ViewModelAudioPlayer::class.java]

        mainThreadHandler = Handler(Looper.getMainLooper())

        statesOfPlaying = StatesOfPlaying.STATE_PAUSED
        binding.audioPlayerPlayButton.isEnabled = false
        binding.audioPlayerPlayButton.setOnClickListener { playBackControl() }
        mainThreadHandler?.post(changeButton())
        mainThreadHandler?.post(changeTimer())
        binding.audioPlayerBackButton.setOnClickListener {
            finish()
        }

        binding.audioPlayerTrackName.text =
            intent.extras?.getString(KEY_TRACK_NAME) ?: getString(R.string.unknownTrack)
        binding.audioPlayerArtistName.text =
            intent.extras?.getString(KEY_TRACK_ARTIST) ?: getString(R.string.unknownArtist)
        binding.audioPlayerTime.text =
            intent.extras?.getString(KEY_TRACK_TIME) ?: getString(R.string.track_time)
        binding.audioPlayerAlbumName.text =
            intent.extras?.getString(KEY_TRACK_ALBUM) ?: getString(R.string.unknownAlbum)
        binding.audioPlayerYearNumber.text =
            (intent.extras?.getString(KEY_TRACK_YEAR) ?: getString(R.string.unknownYear)).take(year)
        binding.audioPlayerGenreName.text =
            intent.extras?.getString(KEY_TRACK_GENRE) ?: getString(R.string.unknownGenre)
        binding.audioPlayerCountryName.text =
            intent.extras?.getString(KEY_TRACK_COUNTRY) ?: getString(R.string.unknownCountry)

        val image = (intent.extras?.getString(KEY_TRACK_COVER))

        if (image != null) {
            image.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(image)
                .placeholder(R.drawable.audio_player_cover)
                .centerCrop()
                .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.icon_padding)))
                .into(binding.audioPlayerCover)
        }

        val url = intent.extras?.getString(URL)
        if (!url.isNullOrEmpty()) viewModelAudioPlayer.createPlayer(url) {
            preparePlayer()
        }
    }

    fun preparePlayer() {
        binding.audioPlayerPlayButton.isEnabled = true
        binding.audioPlayerPlayButton.setImageResource(R.drawable.play_button)
    }

    private fun playBackControl() {
        when (statesOfPlaying) {
            StatesOfPlaying.STATE_PLAYING -> {
                viewModelAudioPlayer.pause()
            }
            StatesOfPlaying.STATE_PREPARED, StatesOfPlaying.STATE_PAUSED -> {
                viewModelAudioPlayer.play()
            }
            else -> {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelAudioPlayer.destroy()
    }

    fun playerVisibility() {
        statesOfPlaying = viewModelAudioPlayer.stateListener()

        when (statesOfPlaying) {
            StatesOfPlaying.STATE_PAUSED -> binding.audioPlayerPlayButton.setImageResource(R.drawable.play_button)
            StatesOfPlaying.STATE_DEFAULT -> binding.audioPlayerPlayButton.setImageResource(R.drawable.play_button)
            StatesOfPlaying.STATE_PREPARED -> binding.audioPlayerPlayButton.setImageResource(R.drawable.play_button)
            StatesOfPlaying.STATE_PLAYING -> binding.audioPlayerPlayButton.setImageResource(R.drawable.audio_player_pause)
        }
    }

    private fun changeButton(): Runnable {
        val changing = Runnable {
            playerVisibility()
            mainThreadHandler?.postDelayed(changeButton(), AUDIO_DELAY_MILLIS)
        }
        return changing
    }

    private fun changeTimer(): Runnable {
        val changing = Runnable {
            binding.audioPlayerTrackTimer.text = viewModelAudioPlayer.time()
            mainThreadHandler?.postDelayed(changeTimer(), AUDIO_DELAY_MILLIS)
        }
        return changing
    }
}