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
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.StatesOfPlaying
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.view_model.ViewModelAudioPlayer

class AudioPlayerActivity : AppCompatActivity() {

    private var mainThreadHandler: Handler? = null
    private var url=""
    private lateinit var viewModelAudioPlayer: ViewModelAudioPlayer
    private lateinit var binding: AudioPlayerBinding
    private lateinit var statesOfPlaying: StatesOfPlaying
    lateinit var playerState: PlayerState

    companion object {
        const val year = 4
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

        viewModelAudioPlayer.playerStateLiveData.observe(this) { playerState ->
            playerVisibility()
        }
        mainThreadHandler = Handler(Looper.getMainLooper())

        statesOfPlaying = StatesOfPlaying.STATE_PAUSED
        playerState = PlayerState(StatesOfPlaying.STATE_PAUSED, "")
        binding.audioPlayerPlayButton.isEnabled = false
        binding.audioPlayerPlayButton.setOnClickListener { playBackControl() }
        mainThreadHandler?.post(changeButton())
        mainThreadHandler?.post(changeTimer())
        binding.audioPlayerBackButton.setOnClickListener {
            finish()
        }

        val track = intent.getParcelableExtra<Track>("track")

        binding.audioPlayerTrackName.text = track?.trackName ?: getString(R.string.unknownTrack)
        binding.audioPlayerArtistName.text = track?.artistName ?: getString(R.string.unknownArtist)
        binding.audioPlayerTime.text = track?.trackTimeMillis ?: getString(R.string.track_time)
        binding.audioPlayerAlbumName.text =
            track?.collectionName ?: getString(R.string.unknownAlbum)
        binding.audioPlayerYearNumber.text =
            (track?.releaseDate ?: getString(R.string.unknownYear)).take(year)
        binding.audioPlayerGenreName.text =
            track?.primaryGenreName ?: getString(R.string.unknownGenre)
        binding.audioPlayerCountryName.text = track?.country ?: getString(R.string.unknownCountry)

        val image = (track?.artworkUrl100 ?: "Unknown Cover").replace(
            "100x100bb.jpg",
            "512x512bb.jpg"
        )
        if (image != "Unknown Cover") {
            image.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(image)
                .centerCrop()
                .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.icon_padding)))
                .placeholder(R.drawable.audio_player_cover)
                .into(binding.audioPlayerCover)
        }
        url = track?.previewUrl ?: return
        viewModelAudioPlayer.createPlayer(url) {
            preparePlayer()
        }
    }

    fun preparePlayer() {
        binding.audioPlayerPlayButton.isEnabled = true
        binding.audioPlayerPlayButton.setImageResource(R.drawable.play_button)
    }

    private fun playBackControl() {
        when (playerState.playingState) {
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
        when (playerState.playingState) {
            StatesOfPlaying.STATE_PAUSED, StatesOfPlaying.STATE_DEFAULT, StatesOfPlaying.STATE_PREPARED -> binding.audioPlayerPlayButton.setImageResource(
                R.drawable.play_button
            )
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