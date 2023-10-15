package com.example.playlistmaker.domain.search

import com.example.playlistmaker.data.search.request_response.Resource
import com.example.playlistmaker.domain.search.model.Track

interface RepositoryTrack {

    fun searching(expression: String): Resource<List<Track>>

}