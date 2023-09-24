package com.example.playlistmaker.ui.search.view_model.states

import com.example.playlistmaker.domain.search.model.Track

sealed class StatesOfSearching {
    object Loading : StatesOfSearching()
    object Search : StatesOfSearching()
    object ErrorConnection : StatesOfSearching()
    object ErrorFound : StatesOfSearching()
    data class SearchAndHistory(var history: List<Track>) : StatesOfSearching()
    data class SearchCompleted(val data: List<Track>) : StatesOfSearching()
}