package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AudioPlayerBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.view_model.ViewModelAudioPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerActivity : AppCompatActivity() {

    val viewModelAudioPlayer by viewModel<ViewModelAudioPlayer>()
    private lateinit var binding: AudioPlayerBinding

    companion object {
        const val year = 4
        const val AUDIO_DELAY_MILLIS = 300L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeButton()

        viewModelAudioPlayer.getPlayerStateLiveData().observe(this) { playerState ->
            playerVisibility(playerState.isPlaying)
        }

        viewModelAudioPlayer.standTime().observe(this) { time ->
            binding.audioPlayerTrackTimer.text = time
        }

        binding.audioPlayerPlayButton.isEnabled = false
        binding.audioPlayerPlayButton.setOnClickListener { viewModelAudioPlayer.playBackControl() }
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
        if (track?.previewUrl == null) return
        viewModelAudioPlayer.createPlayer(track.previewUrl) {
            preparePlayer()
        }
    }

    fun preparePlayer() {
        binding.audioPlayerPlayButton.isEnabled = true
        binding.audioPlayerPlayButton.setImageResource(R.drawable.play_button)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelAudioPlayer.destroy()
    }

    override fun onPause(){
        super.onPause()
        viewModelAudioPlayer.pause()
    }

    fun playerVisibility(isPlaying: Boolean) {
        val resourceId = if (isPlaying) R.drawable.audio_player_pause else R.drawable.play_button
        binding.audioPlayerPlayButton.setImageResource(resourceId)

    }

    private fun changeButton() {
        lifecycleScope.launch {
            delay(AUDIO_DELAY_MILLIS)
        }
    }
}