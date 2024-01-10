package com.example.playlistmaker.domain.playlist.interactor

import android.net.Uri

interface ImagesRepository {
    fun saveImageToPrivateStorage(uri: Uri): Uri
}