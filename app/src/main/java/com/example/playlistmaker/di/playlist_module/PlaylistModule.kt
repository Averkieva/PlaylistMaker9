package com.example.playlistmaker.di.playlist_module

import androidx.room.Room
import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.dbPlaylist.PlaylistDatabase
import com.example.playlistmaker.data.playlist.PlaylistRepositoryImpl
import com.example.playlistmaker.domain.playlist.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.interactor.PlaylistInteractorImpl
import com.example.playlistmaker.domain.playlist.interactor.PlaylistRepository
import com.example.playlistmaker.ui.playlist.view_model.CreateNewPlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistModule = module {

    single {
        Room.databaseBuilder(androidContext(), PlaylistDatabase::class.java, "playlist_table")
            .allowMainThreadQueries()
            .build()
    }

    factory { PlaylistDbConvertor() }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    viewModel {
        CreateNewPlaylistViewModel(get(), get())
    }
}