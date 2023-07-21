package com.example.playlistmaker

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory  {
    companion object {
        const val SEARCH_SHARED_PREFERENCE = "search"
        const val number_10 = 10
    }
    private val savedSearchHistory = App.getSharedPreferences()

    fun editSearchHistory(newSearchHistoryTrack: Track) {
        var json = ""
        if (App.searchHistoryList.contains(newSearchHistoryTrack)) {
            App.searchHistoryList.remove(newSearchHistoryTrack)
        }
        if (json.isNotEmpty()) {
            if (App.searchHistoryList.isEmpty() && savedSearchHistory.contains(SEARCH_SHARED_PREFERENCE)) {
                val a = object : TypeToken<ArrayList<Track>>() {}.type
                App.searchHistoryList = Gson().fromJson(json, a)
            }
        } else {
            if (App.searchHistoryList.size < number_10) {
                App.searchHistoryList.add(0, newSearchHistoryTrack)
            } else {
                App.searchHistoryList.removeAt(9)
                App.searchHistoryList.add(0, newSearchHistoryTrack)
            }
        }
        json = Gson().toJson(App.searchHistoryList)
        savedSearchHistory.edit().clear().putString(SEARCH_SHARED_PREFERENCE, json).apply()
    }
}