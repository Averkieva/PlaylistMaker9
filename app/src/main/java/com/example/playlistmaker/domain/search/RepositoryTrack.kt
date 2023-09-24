package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track

interface RepositoryTrack {

    fun searching(expression: String): List<Track>

}