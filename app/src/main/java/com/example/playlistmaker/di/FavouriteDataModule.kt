package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.data.FavouriteTrackRepositoryImpl
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.domain.favourite.FavouriteTrackInteractor
import com.example.playlistmaker.domain.favourite.FavouriteTrackInteractorImpl
import com.example.playlistmaker.domain.favourite.FavouriteTrackRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val favouriteDataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .allowMainThreadQueries()
            .build()
    }

    single<FavouriteTrackRepository>
    { FavouriteTrackRepositoryImpl(get(), get()) }

    single<FavouriteTrackInteractor> { FavouriteTrackInteractorImpl(get()) }

}