package com.example.playlistmaker.di.settings_module

import com.example.playlistmaker.data.settings.ThemeImpl
import com.example.playlistmaker.data.share.ExternalNavigatorImpl
import com.example.playlistmaker.domain.settings.interactors.SettingsInteractor
import com.example.playlistmaker.domain.settings.interactors.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.interactors.Theme
import com.example.playlistmaker.domain.share.ExternalNavigator
import com.example.playlistmaker.domain.share.ShareInteractor
import com.example.playlistmaker.domain.share.ShareInteractorImpl
import com.example.playlistmaker.ui.settings.view_model.ViewModelSettings
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sharingModule = module {

    single<Theme> {
        ThemeImpl(get(), get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<ShareInteractor> {
        ShareInteractorImpl(get())
    }

    viewModel { ViewModelSettings(get(), get()) }
}