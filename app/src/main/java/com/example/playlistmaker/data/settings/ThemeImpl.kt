package com.example.playlistmaker.data.settings

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.settings.interactors.Theme

const val KEY = "key"

class ThemeImpl(private val application: App): Theme {
    private lateinit var sharedPref: SharedPreferences
    private var flagTheme: Boolean = false

    override fun changeTheme(): Boolean {
        flagTheme =! flagTheme
        val changing = sharedPref.edit()
        changing.putBoolean(KEY, flagTheme)
        changing.apply()
        return flagTheme
    }

    override fun themeApp(): Boolean {
        sharedPref = application.getSharedPreferences(KEY, MODE_PRIVATE)
        flagTheme = sharedPref.getBoolean(KEY, !darkTheme())
        return flagTheme
    }

    private fun darkTheme():Boolean{
        val realTheme = App.instance.applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return realTheme == Configuration.UI_MODE_NIGHT_YES
    }

}