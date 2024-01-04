package com.example.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AudioPlayerBinding
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.adapter.PlayerAdapter
import com.example.playlistmaker.ui.player.view_model.ViewModelAudioPlayer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {

    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var audioPlayerBinding: AudioPlayerBinding
    private lateinit var bottomNavigator: BottomNavigationView
    private val playerViewModel by viewModel<ViewModelAudioPlayer>()

    companion object {
        const val year = 4
        const val AUDIO_DELAY_MILLIS = 300L
        const val KEY_PLAYER_CREATED = "key_player_created"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        audioPlayerBinding = AudioPlayerBinding.inflate(layoutInflater)

        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.GONE

        audioPlayerBinding.audioPlayerBackButton.setOnClickListener {
            bottomNavigator.visibility = View.VISIBLE
            val back = requireActivity().supportFragmentManager
            bottomNavigator.visibility = View.VISIBLE
            back.popBackStack()
        }

        audioPlayerBinding.createPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.audioPlayerFragment_to_createNewPlaylistFragment)
        }

        return audioPlayerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = arguments?.getParcelable<Track>("track")

        audioPlayerBinding.audioPlayerTrackName.text =
            track?.trackName ?: getString(R.string.unknownTrack)
        audioPlayerBinding.audioPlayerArtistName.text =
            track?.artistName ?: getString(R.string.unknownArtist)
        audioPlayerBinding.audioPlayerTime.text =
            track?.trackTimeMillis ?: getString(R.string.track_time)
        audioPlayerBinding.audioPlayerAlbumName.text =
            track?.collectionName ?: getString(R.string.unknownAlbum)
        audioPlayerBinding.audioPlayerYearNumber.text =
            (track?.releaseDate ?: getString(R.string.unknownYear)).take(year)
        audioPlayerBinding.audioPlayerGenreName.text =
            track?.primaryGenreName ?: getString(R.string.unknownGenre)
        audioPlayerBinding.audioPlayerCountryName.text =
            track?.country ?: getString(R.string.unknownCountry)

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
                .into(audioPlayerBinding.audioPlayerCover)
        }
        if (track?.previewUrl == null) return

        playerViewModel.createPlayer(track.previewUrl) {
            preparePlayer()
        }

        changeButton()

        playerViewModel.getPlayerStateLiveData().observe(requireActivity()) { playerState ->
            playerVisibility(playerState.isPlaying)
        }

        playerViewModel.standTime().observe(requireActivity()) { time ->
            audioPlayerBinding.audioPlayerTrackTimer.text = time
        }

        audioPlayerBinding.audioPlayerPlayButton.isEnabled = false
        audioPlayerBinding.audioPlayerPlayButton.setOnClickListener { playerViewModel.playBackControl() }

        if (savedInstanceState != null) {
            val playerCreated =
                savedInstanceState.getBoolean(KEY_PLAYER_CREATED, false)
            if (playerCreated) {
                preparePlayer()
            }
        }

        audioPlayerBinding.audioPlayerFavouriteButton.setOnClickListener {
            playerViewModel.onFavoriteClicked(track)
        }

        playerViewModel.observeFavourite(track).observe(requireActivity()) { isLiked ->
            if (isLiked)
                audioPlayerBinding.audioPlayerFavouriteButton.setImageResource(R.drawable.audio_player_fav_button)
            else audioPlayerBinding.audioPlayerFavouriteButton.setImageResource(R.drawable.audio_player_no_active_fav)
        }

        val bottomSheetContainer = audioPlayerBinding.standardBottomSheet
        val field = audioPlayerBinding.field
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        field.visibility = View.GONE
                    }

                    else -> {
                        field.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        val recyclerView = audioPlayerBinding.playlistRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        playerViewModel.createPlaylist().observe(viewLifecycleOwner) { playlistList ->
            val adapterData = if (playlistList.isNullOrEmpty()) {
                emptyList()
            } else {
                playlistList
            }
            playerAdapter = PlayerAdapter(adapterData) {
                clicker(track, it)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            recyclerView.adapter = playerAdapter
        }

        audioPlayerBinding.audioPlayerAddButton.setOnClickListener {
            audioPlayerBinding.field.visibility = View.VISIBLE
            bottomSheetBehavior.state = STATE_COLLAPSED
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(
            KEY_PLAYER_CREATED,
            playerViewModel.audioPlayerJob != null
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.destroy()
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
    }

    private fun preparePlayer() {
        if (playerViewModel.audioPlayerJob != null) {
            audioPlayerBinding.audioPlayerPlayButton.isEnabled = true
            audioPlayerBinding.audioPlayerPlayButton.setImageResource(R.drawable.play_button)
        }
    }

    private fun changeButton() {
        lifecycleScope.launch {
            delay(AUDIO_DELAY_MILLIS)
        }
    }

    private fun playerVisibility(isPlaying: Boolean) {
        val resourceId = if (isPlaying) R.drawable.audio_player_pause else R.drawable.play_button
        audioPlayerBinding.audioPlayerPlayButton.setImageResource(resourceId)
    }

    override fun onStop() {
        bottomNavigator.visibility = View.VISIBLE
        super.onStop()
    }

    private fun clicker(track: Track, playlist: Playlist) {
        playerViewModel.add(track, playlist)
        viewLifecycleOwner.lifecycleScope.launch {
            delay(AUDIO_DELAY_MILLIS)
            val playlistName = playlist.playlistName
            val toastMessage = if (playerViewModel.inserted.value == true) {
                "Трек уже добавлен в плейлист $playlistName"
            } else {
                "Добавлено в плейлист $playlistName"
            }
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
        }
    }
}