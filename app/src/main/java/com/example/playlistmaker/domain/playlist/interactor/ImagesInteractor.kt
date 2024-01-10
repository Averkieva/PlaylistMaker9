package com.example.playlistmaker.domain.playlist.interactor

import android.net.Uri

interface ImagesInteractor {
    fun saveImageToPrivateStorage(uri: Uri): Uri
}