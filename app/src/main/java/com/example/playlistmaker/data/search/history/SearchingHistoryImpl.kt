package com.example.playlistmaker.data.search.history

import android.content.Context
import com.example.playlistmaker.domain.search.history.SearchingHistory
import com.example.playlistmaker.domain.search.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchingHistoryImpl(private val context: Context) : SearchingHistory {

    companion object {
        const val SEARCH_SHARED_PREFERENCE = "search"
        const val number_10 = 10
        const val number_9 = 9
        const val number_0 = 0
    }

    private val savedSearchHistory =
        context.getSharedPreferences(SEARCH_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private val gson = Gson()
    private var trackHistoryList = ArrayList<Track>()

    override fun clearHistory() {
        trackHistoryList.clear()
        save()
    }

    override fun add(newIt: Track) {
        val json = ""
        if (json.isNotEmpty()) {
            if (trackHistoryList.isEmpty()) {
                if (savedSearchHistory.contains(SEARCH_SHARED_PREFERENCE)) {
                    val type = object : TypeToken<ArrayList<Track>>() {}.type
                    trackHistoryList = gson.fromJson(json, type)
                }
            }
        }
        if (trackHistoryList.contains(newIt)) {
            trackHistoryList.remove(newIt)
            trackHistoryList.add(number_0, newIt)
        } else {
            if (trackHistoryList.size < number_10)
                trackHistoryList.add(number_0, newIt)
            else {
                trackHistoryList.removeAt(number_9)
                trackHistoryList.add(number_0, newIt)
            }
        }
        save()
    }

    override fun provideSearchHistory(): List<Track> {
        val json = savedSearchHistory.getString(SEARCH_SHARED_PREFERENCE, "")
        if (json != null) {
            if (json.isNotEmpty()) {
                if (trackHistoryList.isEmpty()) {
                    if (savedSearchHistory.contains(SEARCH_SHARED_PREFERENCE)) {
                        val type = object : TypeToken<ArrayList<Track>>() {}.type
                        trackHistoryList = gson.fromJson(json, type)
                    }
                }
            } else
                trackHistoryList = ArrayList()
        }
        return trackHistoryList
    }

    private fun save() {
        var json = ""
        json = gson.toJson(trackHistoryList)
        savedSearchHistory.edit()
            .clear()
            .putString(SEARCH_SHARED_PREFERENCE, json)
            .apply()
    }
}