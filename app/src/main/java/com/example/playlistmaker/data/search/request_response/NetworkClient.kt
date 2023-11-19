package com.example.playlistmaker.data.search.request_response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}