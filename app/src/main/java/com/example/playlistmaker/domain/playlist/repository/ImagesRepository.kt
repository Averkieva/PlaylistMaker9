package com.example.playlistmaker.domain.playlist.repository

import android.net.Uri

interface ImagesRepository {
    fun saveImageToPrivateStorage(uri: Uri): Uri
}