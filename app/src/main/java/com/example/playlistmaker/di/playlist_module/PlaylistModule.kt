package com.example.playlistmaker.di.playlist_module

import androidx.room.Room
import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.dbPlaylist.PlaylistDatabase
import com.example.playlistmaker.data.playlist.PlaylistRepositoryImpl
import com.example.playlistmaker.domain.playlist.interactor.*
import com.example.playlistmaker.domain.playlist.repository.PlaylistRepository
import com.example.playlistmaker.ui.playlist.view_model.CreateNewPlaylistViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistModule = module {

    single {
        Room.databaseBuilder(androidContext(), PlaylistDatabase::class.java, "playlist_table")
            .allowMainThreadQueries()
            .build()
    }

    factory { PlaylistDbConvertor(get()) }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    factory { Gson() }

    single<ImagesInteractor> {
        ImagesInteractorImpl(get())
    }

    viewModel {
        CreateNewPlaylistViewModel(get(), get(), get())
    }
}