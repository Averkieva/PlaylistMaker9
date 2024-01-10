package com.example.playlistmaker.di.playlistInfo_module

import com.example.playlistmaker.data.playlist.PlaylistInfoRepositoryImpl
import com.example.playlistmaker.domain.playlistInfo.PlaylistInfoInteractor
import com.example.playlistmaker.domain.playlistInfo.PlaylistInfoInteractorImpl
import com.example.playlistmaker.domain.playlistInfo.PlaylistInfoRepository
import com.example.playlistmaker.ui.playlist.view_model.EditPlaylistViewModel
import com.example.playlistmaker.ui.playlistInfo.view_model.PlaylistInfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistInfoModule = module {
    single<PlaylistInfoInteractor> { PlaylistInfoInteractorImpl(get()) }

    single<PlaylistInfoRepository> { PlaylistInfoRepositoryImpl(get()) }

    viewModel { PlaylistInfoViewModel(get(), get(), get()) }

    viewModel { EditPlaylistViewModel(get(), get()) }

}