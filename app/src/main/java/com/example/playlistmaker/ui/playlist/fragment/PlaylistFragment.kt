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
import com.example.playlistmaker.ui.mediateka.adapter.PlaylistAdapter
import com.example.playlistmaker.ui.playlist.view_model.PlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private lateinit var bottomNavigator: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.VISIBLE

        binding.playlists.visibility = View.VISIBLE
        if (playlistViewModel.getPlayerStateLiveData().value.isNullOrEmpty())
            binding.playlists.visibility = View.GONE


        binding.newPlaylistButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.createNewPlaylistFragment)
        }

        val recyclerView = binding.playlists
        recyclerView.adapter =
            playlistViewModel.getPlayerStateLiveData().value?.let { PlaylistAdapter(it) {} }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistViewModel.createPlaylist().observe(viewLifecycleOwner) { item ->
            val isPlaylistEmpty = item.isNullOrEmpty()

            binding.emptyPlaylist.visibility = if (isPlaylistEmpty) View.VISIBLE else View.GONE
            binding.playlists.visibility = if (isPlaylistEmpty) View.GONE else View.VISIBLE
            binding.emptyText.visibility = if (isPlaylistEmpty) View.VISIBLE else View.GONE

            if (!isPlaylistEmpty) {
                binding.playlists.adapter = PlaylistAdapter(item) {}
            }
        }
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}