package com.example.playlistmaker.data.search.request_response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}