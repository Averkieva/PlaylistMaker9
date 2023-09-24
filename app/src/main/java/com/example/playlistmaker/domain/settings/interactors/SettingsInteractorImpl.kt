package com.example.playlistmaker.domain.settings.interactors

import com.example.playlistmaker.creator.Creator

class SettingsInteractorImpl (private var theme: Theme) : SettingsInteractor {

    var flagDark = true

    init{
        theme = Creator.provideTheme()
    }

    override fun changeTheme(): Boolean {
        flagDark = theme.changeTheme()
        return flagDark
    }

    override fun isDark(): Boolean {
        flagDark = theme.themeApp()
        return flagDark
    }

}