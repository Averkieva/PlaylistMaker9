package com.example.playlistmaker.domain.playlist.interactor

import android.net.Uri

class ImagesInteractorImpl(private val repository: ImagesRepository) : ImagesInteractor {

    override fun saveImageToPrivateStorage(uri: Uri): Uri {
        return repository.saveImageToPrivateStorage(uri)
    }
}