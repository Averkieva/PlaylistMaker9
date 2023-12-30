package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.favouriteDataModule
import com.example.playlistmaker.di.mediateka_module.mediatekaModule
import com.example.playlistmaker.di.player_module.playerModule
import com.example.playlistmaker.di.searching_module.dataModule
import com.example.playlistmaker.di.searching_module.repositoryTrackModule
import com.example.playlistmaker.di.searching_module.searchInteractorModule
import com.example.playlistmaker.di.searching_module.viewModelSearchingModule
import com.example.playlistmaker.di.settings_module.sharingModule
import com.example.playlistmaker.domain.settings.interactors.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class App : Application(), KoinComponent {

    private var flagTheme: Boolean = false

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                playerModule,
                dataModule,
                repositoryTrackModule,
                searchInteractorModule,
                viewModelSearchingModule,
                sharingModule,
                mediatekaModule,
                favouriteDataModule
            )

        }
        val settingsInteractor = getKoin().get<SettingsInteractor>()
        instance = this

        flagTheme = settingsInteractor.isDark()
        installTheme(flagTheme)
    }

    private fun installTheme(flagTheme: Boolean) {
        if (flagTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}
