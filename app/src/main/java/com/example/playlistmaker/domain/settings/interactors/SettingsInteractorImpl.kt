package com.example.playlistmaker.domain.settings.interactors

class SettingsInteractorImpl(private var theme: Theme) : SettingsInteractor {

    var flagDark = true

    override fun changeTheme(): Boolean {
        flagDark = theme.changeTheme()
        return flagDark
    }

    override fun isDark(): Boolean {
        flagDark = theme.themeApp()
        return flagDark
    }

}