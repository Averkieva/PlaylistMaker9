package com.example.playlistmaker.di.player_module

import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.ui.player.view_model.ViewModelAudioPlayer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    factory<PlayerRepository> {
        PlayerRepositoryImpl()
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    viewModel { ViewModelAudioPlayer(get()) }
}