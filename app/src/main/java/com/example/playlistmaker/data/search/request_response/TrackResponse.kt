package com.example.playlistmaker.data.search.request_response

data class TrackResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()