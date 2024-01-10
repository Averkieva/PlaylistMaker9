package com.example.playlistmaker.di.searching_module

import android.content.Context
import com.example.playlistmaker.data.playlist.ImagesRepositoryImpl
import com.example.playlistmaker.data.search.history.SearchingHistoryImpl.Companion.SEARCH_SHARED_PREFERENCE
import com.example.playlistmaker.data.search.request_response.ITunesApi
import com.example.playlistmaker.data.search.request_response.NetworkClient
import com.example.playlistmaker.data.search.request_response.RetrofitNetworkClient
import com.example.playlistmaker.domain.playlist.repository.ImagesRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesApi> {

        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)

    }

    single {
        androidContext()
            .getSharedPreferences(SEARCH_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single<ImagesRepository> { ImagesRepositoryImpl(get()) }
}