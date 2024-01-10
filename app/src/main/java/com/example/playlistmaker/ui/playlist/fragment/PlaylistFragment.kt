package com.example.playlistmaker.ui.playlist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.ui.mediateka.adapter.PlaylistAdapter
import com.example.playlistmaker.ui.playlist.view_model.PlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var bottomNavigator: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.VISIBLE

        binding.playlists.visibility = View.VISIBLE

        binding.newPlaylistButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.createNewPlaylistFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistAdapter = PlaylistAdapter {
            clicker(it)
        }

        val recyclerView = binding.playlists
        recyclerView.adapter =
            playlistAdapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        if (playlistViewModel.getPlayerStateLiveData().value.isNullOrEmpty())
            binding.playlists.visibility = View.GONE

        playlistViewModel.createPlaylist()

        playlistViewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) { item ->

            if (item.isNullOrEmpty()){
                binding.emptyPlaylist.visibility = View.VISIBLE
                binding.emptyText.visibility = View.VISIBLE
                binding.playlists.visibility = View.GONE

                return@observe
            }
            else{
                recyclerView.adapter = playlistAdapter
                playlistAdapter.setIt(item)
                binding.emptyPlaylist.visibility = View.GONE
                binding.emptyText.visibility = View.GONE
                binding.playlists.visibility = View.VISIBLE

                return@observe
            }
        }
    }

    private fun clicker(playlist: Playlist) {
        val bundle = Bundle()
        bundle.putParcelable("playlist", playlist)
        val navController = findNavController()
        navController.navigate(R.id.mediatekaFragment_to_playlistInfoFragment, bundle)
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}