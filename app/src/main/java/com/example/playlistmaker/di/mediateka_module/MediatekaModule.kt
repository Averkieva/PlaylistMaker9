package com.example.playlistmaker.di.mediateka_module

import com.example.playlistmaker.ui.mediateka.view_model.FavouriteTracksViewModel
import com.example.playlistmaker.ui.mediateka.view_model.MediatekaViewModel
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediatekaModule = module {
    viewModel { FavouriteTracksViewModel() }
    viewModel { MediatekaViewModel() }
    viewModel { PlaylistViewModel() }
}