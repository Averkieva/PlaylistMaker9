package com.example.playlistmaker.domain.playlist.interactor

import android.net.Uri
import com.example.playlistmaker.domain.playlist.repository.ImagesRepository

class ImagesInteractorImpl(private val repository: ImagesRepository) : ImagesInteractor {

    override fun saveImageToPrivateStorage(uri: Uri): Uri {
        return repository.saveImageToPrivateStorage(uri)
    }
}