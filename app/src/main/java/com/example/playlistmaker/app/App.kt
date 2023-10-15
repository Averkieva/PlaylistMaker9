package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App : Application() {

    private var flagTheme: Boolean = false

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Creator.init(this)

        val settingsInteractor = Creator.provideSettingsInteractor()
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
