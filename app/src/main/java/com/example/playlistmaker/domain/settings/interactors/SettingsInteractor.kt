package com.example.playlistmaker.domain.settings.interactors

interface SettingsInteractor {

    fun changeTheme(): Boolean

    fun isDark(): Boolean

}