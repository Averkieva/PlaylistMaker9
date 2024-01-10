package com.example.playlistmaker.ui.playlistInfo.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistInfoBinding
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.playlistInfo.view_model.PlaylistInfoViewModel
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistInfoFragment : Fragment() {
    private var isClickAllowed = true
    private lateinit var playlistInfoBinding: PlaylistInfoBinding
    private val playlistInfoViewModel by viewModel<PlaylistInfoViewModel>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var bottomNavigator: BottomNavigationView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        playlistInfoBinding = PlaylistInfoBinding.inflate(inflater, container, false)

        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.GONE

        playlistInfoBinding.playlistInfoBackButton.setOnClickListener {
            bottomNavigator.visibility = View.VISIBLE
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            bottomNavigator.visibility = View.VISIBLE
            findNavController().popBackStack()
        }
        return playlistInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlist = arguments?.getParcelable<Playlist>("playlist")
        if (playlist != null) {
            playlist.playlistId?.let { playlistInfoViewModel.getPlaylist(it) }
        }
        val checkedPlaylist = playlist?.let { playlistInfo(it) }
        if (checkedPlaylist != null) {
            playlistAlbum(checkedPlaylist)
            menuBottomSheet()
        }
    }

    private fun clicker(track: Track) {
        val bundle = Bundle()
        bundle.putParcelable("track", track)
        val navController = findNavController()
        navController.navigate(R.id.playlistInfoFragment_to_audioPlayerFragment, bundle)
    }

    private fun playlistDuration(playlist: Playlist) {
        playlistInfoViewModel.getTime(playlist)
        playlistInfoViewModel.getDuration().observe(viewLifecycleOwner) { time ->
            playlistInfoBinding.playlistInfoDuration.text = time
        }
    }

    private fun playlistInfo(item: Playlist): Playlist {
        var playlists = item
        playlistInfoViewModel.getEdit().observe(viewLifecycleOwner) { edit ->
            playlists = edit
            playlistInfoBinding.playlistInfoTitle.text = playlists.playlistName
            playlistInfoBinding.playlistInfoDescription.text = playlists.playlistDescription ?: ""
            playlistDuration(playlists)
            playlistInfoBinding.numberOfTracksMenu.text =
                context?.applicationContext?.resources?.getQuantityString(
                    R.plurals.tracks,
                    playlists.trackList.size,
                    playlists.trackList.size
                )
            playlistAlbum(playlists)
            bottomSheetDraw(playlists)
        }
        return playlists
    }

    private fun playlistAlbum(playlist: Playlist) {
        val getImage = playlist.playlistUri
        val isImageAvailable = getImage != "null"

        playlistInfoBinding.playlistInfoPlaceholder.visibility =
            if (isImageAvailable) View.GONE else View.VISIBLE

        if (isImageAvailable) {
            val baseSize = resources.getDimensionPixelSize(R.dimen.width_and_height_media)
            Glide.with(this)
                .load(getImage)
                .centerCrop()
                .transform(CenterCrop())
                .override(baseSize, baseSize)
                .into(playlistInfoBinding.playlistInfoAlbum)
        }
    }

    private fun bottomSheetDraw(playlist: Playlist) {
        val bottomSheetBehavior =
            BottomSheetBehavior.from(playlistInfoBinding.playlistInfoTrackLayout).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }
        trackAdapter = TrackAdapter(
            clickListener = { if (isClickAllowed) clicker(it) },
            longClickListener = { dialogDelete(it, playlist) }
        )
        playlistInfoViewModel.getTrack(playlist)
        tracksVisibility()

        with(playlistInfoBinding.playlistInfoTrackRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }
        with(playlistInfoBinding) {
            playlistInfoShare.setOnClickListener { share(playlist) }
            shareMenu.setOnClickListener { share(playlist) }
            editInformation.setOnClickListener {
                val bundle = Bundle().apply { putParcelable("playlist", playlist) }
                findNavController().navigate(
                    R.id.playlistInfoFragment_to_editPlaylistFragment,
                    bundle
                )
            }
            deletePlaylist.setOnClickListener { deletePlaylistDialog(playlist) }
        }
    }


    private fun dialogDelete(track: Track, playlist: Playlist) {
        val isDarkTheme = playlistInfoViewModel.isDark()
        val textColor = if (isDarkTheme) Color.BLACK else Color.WHITE

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.question_delete_track)
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ -> delete(track, playlist) }
            .show()
            .apply {
                getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor)
                getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor)
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun delete(item: Track, playlist: Playlist) {
        playlistInfoViewModel.getTrack(playlist)
        playlistInfoViewModel.deleteTrack(item, playlist)
        playlistInfo(playlist)
        tracksVisibility()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun tracksVisibility() {
        playlistInfoViewModel.getTracksLiveData().observe(viewLifecycleOwner) { trackList ->
            val isTrackListEmpty = trackList.isNullOrEmpty()
            playlistInfoBinding.emptyTracks.visibility =
                if (isTrackListEmpty) View.VISIBLE else View.GONE
            playlistInfoBinding.playlistInfoTrackRecyclerView.visibility =
                if (isTrackListEmpty) View.GONE else View.VISIBLE
            if (!isTrackListEmpty) {
                trackAdapter.setIt(trackList)
            }
        }
    }


    private fun deletePlaylist(item: Playlist) {
        playlistInfoViewModel.deletePlaylist(item)
        val navController = findNavController()
        navController.navigate(R.id.playlistInfoFragment_to_mediatekaFragment_2)
    }

    private fun deletePlaylistDialog(playlist: Playlist) {
        val isDark = playlistInfoViewModel.isDark()
        val textColor = if (isDark) Color.BLACK else Color.WHITE
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.question_delete_playlist)
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ -> deletePlaylist(playlist) }
            .show()
            .apply {
                getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor)
                getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor)
            }
    }

    private fun share(playlist: Playlist) {
        val trackList = playlistInfoViewModel.getTracksLiveData().value ?: emptyList()
        if (trackList.isEmpty()) {
            Toast.makeText(
                requireContext(),
                R.string.nothing_for_sharing,
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val trackInfo = buildString {
            append("${playlist.playlistName} \n ${playlist.playlistDescription} \n ${trackList.size} треков \n")

            trackList.forEachIndexed { index, track ->
                append("${index + 1}. ${track.trackName} - (${track.trackTimeMillis}) \n")
            }
        }
        val intentSend = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, trackInfo)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Intent.createChooser(this, null)
        }
        requireContext().startActivity(intentSend, null)
    }


    private fun menuBottomSheet() {
        val menuBottomSheet = playlistInfoBinding.playlistInfoMenuLayout
        val field = playlistInfoBinding.playlistInfoField
        val menuBottomSheetBehavior = BottomSheetBehavior.from(menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    field.visibility =
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
        playlistInfoBinding.more.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    companion object {
        fun newInstance() = PlaylistInfoFragment()
    }

}