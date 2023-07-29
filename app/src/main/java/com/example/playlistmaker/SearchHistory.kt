package com.example.playlistmaker

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory {
    companion object {
        const val SEARCH_SHARED_PREFERENCE = "search"
        const val number_10 = 10
    }

    private val savedSearchHistory = App.getSharedPreferences()

    fun editSearchHistory(newSearchHistoryTrack: Track) {
        read()
        if (App.searchHistoryList.contains(newSearchHistoryTrack)){
            App.searchHistoryList.remove(newSearchHistoryTrack)
        }
        if(App.searchHistoryList.size == number_10){
            App.searchHistoryList.removeAt(9)
        }
        App.searchHistoryList.add(0, newSearchHistoryTrack)
        write()
    }

     fun read(): ArrayList<Track> {
         val emptyList = Gson().toJson(ArrayList<Track>())
         return Gson().fromJson(
             App.savedSearchHistory.getString(SEARCH_SHARED_PREFERENCE, emptyList),
             object : TypeToken<ArrayList<Track>>() {
             }.type
         )
    }
    private fun write(){
        val json = Gson().toJson(App.searchHistoryList)
        savedSearchHistory.edit().clear().putString(SEARCH_SHARED_PREFERENCE,json).apply()
    }
}