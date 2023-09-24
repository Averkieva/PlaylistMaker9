package com.example.playlistmaker.domain.settings.interactors

interface Theme {
    fun changeTheme(): Boolean

    fun themeApp(): Boolean
}