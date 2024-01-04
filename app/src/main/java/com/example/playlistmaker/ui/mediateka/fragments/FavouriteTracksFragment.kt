package com.example.playlistmaker.ui.mediateka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.mediateka.view_model.FavouriteTracksViewModel
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }

    private fun clickAdapting(track: Track) {
        favouriteTracksViewModel.add(track)
        val bundle = Bundle()
        bundle.putParcelable("track", track)
        val navController = findNavController()
        navController.navigate(R.id.audioPlayerFragment, bundle)
    }

    private lateinit var binding: FragmentFavouriteTracksBinding
    private val favouriteTracksViewModel by viewModel<FavouriteTracksViewModel>()

    private var isClickAllowed = true
    private val favouriteTracksAdapter: TrackAdapter by lazy {
        TrackAdapter {
            if (isClickAllowed) {
                clickAdapting(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        binding.favouriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favouriteRecyclerView.adapter = favouriteTracksAdapter
        binding.emptyMediateka.visibility = View.GONE
        binding.emptyMediatekaText.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteTracksViewModel.createFavourite().observe(viewLifecycleOwner) {
            if (favouriteTracksViewModel.getResults().value.isNullOrEmpty()) {
                binding.emptyMediateka.visibility = View.VISIBLE
                binding.favouriteRecyclerView.visibility = View.GONE
                binding.emptyMediatekaText.visibility = View.VISIBLE
                favouriteTracksAdapter.notifyDataSetChanged()
            } else {
                binding.emptyMediateka.visibility = View.GONE
                binding.favouriteRecyclerView.visibility = View.VISIBLE
                binding.emptyMediatekaText.visibility = View.GONE
                favouriteTracksAdapter.setIt(favouriteTracksViewModel.getResults().value!!)
                favouriteTracksAdapter.notifyDataSetChanged()
            }
        }
    }
}