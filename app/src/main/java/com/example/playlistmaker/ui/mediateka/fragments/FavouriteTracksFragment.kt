package com.example.playlistmaker.ui.mediateka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.example.playlistmaker.ui.mediateka.view_model.FavouriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }

    private lateinit var binding: FragmentFavouriteTracksBinding
    private val favouriteTracksViewModel by viewModel<FavouriteTracksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }
}