package com.example.playlistmaker.data.search.request_response

import com.example.playlistmaker.domain.search.ErrorType

sealed class Resource<T>(val data: T? = null, val message: ErrorType? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: ErrorType?, data: T? = null) : Resource<T>(data, message)
}