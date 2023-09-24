package com.example.playlistmaker.creator

import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.search.RepositoryTrackImpl
import com.example.playlistmaker.data.search.history.SearchingHistoryImpl
import com.example.playlistmaker.data.search.request_response.NetworkClient
import com.example.playlistmaker.data.search.request_response.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.ThemeImpl
import com.example.playlistmaker.data.share.ExternalNavigatorImpl
import com.example.playlistmaker.domain.search.RepositoryTrack
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.history.SearchingHistory
import com.example.playlistmaker.domain.search.interactors.SearchingInteractor
import com.example.playlistmaker.domain.search.interactors.SearchingInteractorImpl
import com.example.playlistmaker.domain.settings.interactors.SettingsInteractor
import com.example.playlistmaker.domain.settings.interactors.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.interactors.Theme
import com.example.playlistmaker.domain.share.ExternalNavigator
import com.example.playlistmaker.domain.share.ShareInteractor
import com.example.playlistmaker.domain.share.ShareInteractorImpl

object Creator {
    private lateinit var application: App

    fun init(application: App) {
        this.application = application
    }

    fun provideSearchingInteractor(): SearchingInteractor{
        return SearchingInteractorImpl(provideRepositoryTrack())
    }

    fun provideRepositoryTrack(): RepositoryTrack{
        return RepositoryTrackImpl(RetrofitNetworkClient())
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }

    fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun provideSearchHistory():SearchingHistory{
        return SearchingHistoryImpl(application)
    }

    fun provideTheme(): Theme {
        return ThemeImpl(application)
    }

    fun provideSettingsInteractor():SettingsInteractor{
        return SettingsInteractorImpl(provideTheme())
    }

    fun provideSearchHistoryInteractor():SearchHistoryInteractor{
        return SearchHistoryInteractorImpl(provideSearchHistory())
    }
    fun provideExternalNavigator():ExternalNavigator{
        return ExternalNavigatorImpl(application)
    }

    fun provideShareInteractor(): ShareInteractor{
        return ShareInteractorImpl(provideExternalNavigator())
    }
}