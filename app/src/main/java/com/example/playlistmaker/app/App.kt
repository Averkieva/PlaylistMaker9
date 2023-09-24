package com.example.playlistmaker.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.SearchHistory
import com.example.playlistmaker.domain.models.Track

class App : Application() {
    companion object {
        lateinit var savedSearchHistory: SharedPreferences
        fun getSharedPreferences(): SharedPreferences {
            return savedSearchHistory
        }
        var searchHistoryList = ArrayList<Track>()
    }
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        savedSearchHistory = applicationContext.getSharedPreferences(SearchHistory.SEARCH_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
